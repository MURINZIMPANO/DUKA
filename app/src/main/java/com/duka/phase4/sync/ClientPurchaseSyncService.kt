package com.duka.phase4.sync

import com.duka.app.data.local.dao.ClientPurchaseDao
import com.duka.app.data.local.entity.ClientPurchase
import com.duka.phase3.sync.SupabaseConfig
import com.duka.phase3.sync.SupabaseRestClient
import com.duka.phase3.sync.dbl
import com.duka.phase3.sync.longOrNull
import com.duka.phase3.sync.str
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Phase 4 — Cross-device client purchases, modeled on Phase 3's ClientChatService.
 *
 * Local-first for writes: the purchase is inserted into Room the moment the
 * client taps Buy, so the flow works fully offline. When connectivity allows:
 *   PUSH (up):   confirmed purchases → Supabase `client_sales` (idempotent — the
 *                deterministic id means repeated pushes upsert, not duplicate).
 *   PULL (down): owner device pulls its shop's client sales and imports them as
 *                local Sale rows (source = "client") so income/top products
 *                update through the same Room flows the dashboard already uses.
 *
 * NON-FUNCTIONAL SPEC (binding): sync failures are VISIBLE and retriable —
 * never silent. [syncState] exposes exactly what happened; `pendingPush` on the
 * ClientPurchase row is the durable "Purchase pending — will complete when
 * you're back online" state.
 *
 * HONESTY NOTE: stock and income on the CLIENT device are remote shops' data —
 * the client's device only records the purchase locally and pushes it. The
 * owner's device performs the authoritative stock decrement when the purchase
 * arrives, reusing the exact repository functions Employee Sell uses.
 */
@Singleton
class ClientPurchaseSyncService @Inject constructor(
    private val dao: ClientPurchaseDao,
    private val client: SupabaseRestClient = SupabaseRestClient()
) {
    data class SyncState(
        val inProgress: Boolean = false,
        val lastError: String? = null,
        val configured: Boolean = SupabaseConfig.IS_CONFIGURED
    )

    private val _syncState = MutableStateFlow(SyncState())
    val syncState: StateFlow<SyncState> = _syncState

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Record a purchase: insert locally first (local-first), then push.
     * Returns the purchase row so the caller can navigate to its receipt.
     * If the push fails, the row stays pending and [syncState] carries the
     * visible error; retryPending() completes it when connectivity returns.
     */
    suspend fun recordPurchase(
        shopRemoteId: String,
        shopName: String,
        productName: String,
        unitPrice: Double,
        quantity: Int,
        clientUserId: Long
    ): ClientPurchase {
        val timestamp = System.currentTimeMillis()
        val purchase = ClientPurchase(
            remoteId = purchaseUuid(shopRemoteId, clientUserId, timestamp),
            shopRemoteId = shopRemoteId,
            shopName = shopName,
            productName = productName,
            unitPrice = unitPrice,
            quantity = quantity,
            total = unitPrice * quantity,
            receiptNumber = "",   // filled by the EBM mock on the receipt screen
            clientUserId = clientUserId,
            timestamp = timestamp,
            pendingPush = true,
            importedSaleId = 0
        )
        dao.upsert(purchase)   // local-first: the purchase exists even fully offline
        pushOne(purchase)
        return dao.getByRemoteId(purchase.remoteId) ?: purchase
    }

    /** Retry any locally-pending purchases (e.g. after connectivity returns). */
    suspend fun retryPending() {
        dao.pendingAll().forEach { pushOne(it) }
    }

    private suspend fun pushOne(purchase: ClientPurchase) {
        if (!SupabaseConfig.IS_CONFIGURED) {
            _syncState.value = SyncState(
                lastError = "Backend not configured — purchase saved on this device only",
                configured = false
            )
            return
        }
        _syncState.value = _syncState.value.copy(inProgress = true, lastError = null)
        val payload = buildJsonObject {
            put("id", purchase.remoteId)
            put("shop_id", purchase.shopRemoteId)
            put("shop_name", purchase.shopName)
            put("product_name", purchase.productName)
            put("unit_price", purchase.unitPrice)
            put("quantity", purchase.quantity)
            put("total", purchase.total)
            put("receipt_number", purchase.receiptNumber)
            put("client_user_id", purchase.clientUserId)
            put("timestamp_millis", purchase.timestamp)
        }
        val res = client.post("client_sales", json.encodeToString(JsonObject.serializer(), payload))
        when (res) {
            is SupabaseRestClient.Result.Success -> {
                dao.confirmPush(purchase.remoteId)
                _syncState.value = SyncState(configured = true)
            }
            is SupabaseRestClient.Result.Failure -> {
                _syncState.value = SyncState(
                    lastError = "Purchase not yet synced: ${res.message}",
                    configured = true
                )
            }
        }
    }

    /**
     * Deterministic UUID-shaped remote id so repeated pushes upsert, not duplicate.
     * Same shape/strategy as Phase 3's shop + message ids (no external deps).
     */
    private fun purchaseUuid(shopRemoteId: String, clientUserId: Long, timestamp: Long): String {
        val bytes = MessageDigest.getInstance("MD5")
            .digest("duka-purchase-$shopRemoteId-$clientUserId-$timestamp".toByteArray())
        val s = bytes.joinToString("") { "%02x".format(it) }
        return "${s.substring(0, 8)}-${s.substring(8, 12)}-${s.substring(12, 16)}-${s.substring(16, 20)}-${s.substring(20, 32)}"
    }
}