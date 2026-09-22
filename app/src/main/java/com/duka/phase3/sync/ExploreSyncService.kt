package com.duka.phase3.sync

import com.duka.app.data.local.dao.ExploreShopDao
import com.duka.app.data.local.entity.ExploreShop
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.SaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.put
import kotlinx.serialization.json.JsonNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Phase 3 — Local-first sync between each shop's Room DB and the shared Supabase backend.
 *
 * Local-first for writes: the shop keeps operating fully offline. When connectivity
 * allows, this service:
 *   PUSH (up):  the shop's public profile + product catalog + aggregate sales velocity
 *               (counts only — individual sale records, employee chat, and tax/EBM data
 *               never leave the device).
 *   PULL (down): other shops' public profiles for the Explore screen into the local cache.
 *
 * NON-FUNCTIONAL SPEC: sync failures are VISIBLE and retriable — never silent.
 * [syncState] exposes exactly what happened so UI can show a "not yet synced" state.
 */
class ExploreSyncService(
    private val businessRepository: BusinessRepository,
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    private val exploreShopDao: ExploreShopDao,
    private val client: SupabaseRestClient = SupabaseRestClient()
) {
    data class SyncState(
        val inProgress: Boolean = false,
        val lastSuccessAt: Long = 0,
        val lastError: String? = null,
        val lastErrorRetriable: Boolean = false,
        val configured: Boolean = SupabaseConfig.IS_CONFIGURED
    )

    private val _syncState = MutableStateFlow(SyncState())
    val syncState: StateFlow<SyncState> = _syncState

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Full sync cycle: push own shop up, then pull the Explore directory down.
     * Safe to call repeatedly; every call updates the visible state.
     */
    suspend fun syncNow(district: String) {
        if (!SupabaseConfig.IS_CONFIGURED) {
            _syncState.value = SyncState(
                lastError = "Backend not configured — add your Supabase URL and anon key in SupabaseConfig.kt",
                lastErrorRetriable = true
            )
            return
        }
        _syncState.value = _syncState.value.copy(inProgress = true, lastError = null)
        val pushError = pushOwnShop()
        val pullError = if (pushError == null) pullExploreDirectory(district) else null
        _syncState.value = if (pushError == null && pullError == null) {
            SyncState(inProgress = false, lastSuccessAt = System.currentTimeMillis(), configured = true)
        } else {
            SyncState(
                inProgress = false,
                lastError = pushError ?: pullError,
                lastErrorRetriable = true,
                configured = true
            )
        }
    }

    /**
     * PUSH — upsert the shop's public profile, then its catalog.
     * Returns null on success, or a human-readable error string.
     */
    private suspend fun pushOwnShop(): String? {
        val business = businessRepository.getActiveBusinessOnce() ?: return null // fresh install, nothing to push
        val products = productRepository.getAllActiveByBusiness(business.id)
        val shopRemoteId = stableShopUuid(business.id, business.createdAt)

        val avgPrice = if (products.isEmpty()) 0.0 else products.sumOf { it.price } / products.size
        val velocity = salesVelocityPerDay(business.id)

        val shopPayload = buildJsonObject {
            put("id", shopRemoteId)
            put("name", business.name)
            put("category", business.type)
            put("district", business.district)
            put("avg_price", avgPrice)
            put("sales_velocity", velocity)
            put("product_count", products.size)
            put("registered_at", isoUtc(business.createdAt))
            put("registered_at_millis", business.createdAt)
            put("cover_url", "")
        }

        // Upsert on id conflict (idempotent — repeated pushes update, not duplicate).
        val shopRes = client.post("shops", json.encodeToString(JsonObject.serializer(), shopPayload))
        if (shopRes is SupabaseRestClient.Result.Failure) return "Profile not yet synced: ${shopRes.message}"

        // Push catalog (public fields only: name, price, category).
        if (products.isNotEmpty()) {
            val catalog = products.map { p ->
                buildJsonObject {
                    put("shop_id", shopRemoteId)
                    put("name", p.name)
                    put("price", p.price)
                    put("category", p.category)
                }
            }
            val catalogRes = client.post(
                "shop_products",
                json.encodeToString(kotlinx.serialization.builtins.ListSerializer(JsonObject.serializer()), catalog),
                preferReturn = "params=resolution=merge-duplicates"
            )
            if (catalogRes is SupabaseRestClient.Result.Failure) {
                return "Catalog not yet synced: ${catalogRes.message}"
            }
        }
        return null
    }

    /**
     * PULL — refresh the local Explore cache with every unflagged shop's public profile.
     * Cached/last-synced data remains browsable when offline (Room is the source for UI).
     */
    private suspend fun pullExploreDirectory(district: String): String? {
        val res = client.get(
            table = "shops",
            filters = mapOf("is_flagged" to "eq.false"),
            order = "registered_at.desc",
            limit = 500
        )
        if (res is SupabaseRestClient.Result.Failure) return "Explore not updated: ${res.message}"

        if (res is SupabaseRestClient.Result.Success) {
            val shops = client.parseArray(res.body).mapNotNull { el ->
                val o = el.jsonObject
                try {
                    ExploreShop(
                        remoteId = o.str("id"),
                        name = o.str("name"),
                        category = o.str("category"),
                        district = o.str("district"),
                        coverImageUrl = o.str("cover_url"),
                        ratingAverage = o.dbl("rating_average"),
                        ratingCount = o.intOrNull("rating_count"),
                        avgPrice = o.dbl("avg_price"),
                        salesVelocity = o.dbl("sales_velocity"),
                        productCount = o.intOrNull("product_count"),
                        registeredAt = o.longOrNull("registered_at_millis"),
                        isFlagged = false,
                        syncedAt = System.currentTimeMillis()
                    )
                } catch (_: Exception) {
                    null // malformed row — skip rather than crash the whole cache refresh
                }
            }
            exploreShopDao.upsertAll(shops)
        }
        return null
    }

    /**
     * Rough recent sales velocity — an AGGREGATE, not individual transactions.
     * Sales stay local-only; only the count-per-day leaves the device.
     */
    private suspend fun salesVelocityPerDay(businessId: Long): Double {
        return try {
            val since = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
            val sales = saleRepository.getSalesInRangeOnce(businessId, since, System.currentTimeMillis())
            sales.size / 7.0 // recent sales per day — an aggregate, never individual records
        } catch (_: Exception) {
            0.0
        }
    }

    private fun isoUtc(epochMillis: Long): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }.format(Date(epochMillis))

    /**
     * Deterministic UUID-shaped remote id derived from the local business row,
     * so repeated pushes upsert the same remote row. Simple, no external deps.
     * (Deliberate choice: stable identity without requiring Supabase to mint ids.)
     */
    private fun stableShopUuid(localBusinessId: Long, createdAt: Long): String {
        val bytes = java.security.MessageDigest.getInstance("MD5")
            .digest("duka-shop-$localBusinessId-$createdAt".toByteArray())
        val s = bytes.joinToString("") { "%02x".format(it) }
        return "${s.substring(0, 8)}-${s.substring(8, 12)}-${s.substring(12, 16)}-${s.substring(16, 20)}-${s.substring(20, 32)}"
    }
}

// Small JsonObject readers that tolerate missing/mistyped fields.
internal fun JsonObject.str(key: String): String =
    (this[key] as? JsonPrimitive)?.takeIf { it !is kotlinx.serialization.json.JsonNull }?.content ?: ""

internal fun JsonObject.dbl(key: String): Double =
    (this[key] as? JsonPrimitive)?.takeIf { it !is kotlinx.serialization.json.JsonNull }?.content?.toDoubleOrNull() ?: 0.0

internal fun JsonObject.intOrNull(key: String): Int =
    (this[key] as? JsonPrimitive)?.takeIf { it !is kotlinx.serialization.json.JsonNull }?.content?.toIntOrNull() ?: 0

internal fun JsonObject.longOrNull(key: String): Long =
    (this[key] as? JsonPrimitive)?.takeIf { it !is kotlinx.serialization.json.JsonNull }?.content?.toLongOrNull() ?: 0L
