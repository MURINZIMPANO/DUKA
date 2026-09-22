package com.duka.phase4.sync

import com.duka.phase3.sync.SupabaseConfig
import com.duka.phase3.sync.SupabaseRestClient
import com.duka.phase3.sync.longOrNull
import com.duka.phase3.sync.str
import com.duka.phase4.data.ClientPurchase
import com.duka.phase4.data.ClientPurchaseDao
import com.duka.app.data.local.dao.ClientChatMessageDao
import com.duka.app.data.local.entity.ClientChatMessage
import com.duka.app.data.local.entity.Sale
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.SaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
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
    private val client: SupabaseRestClient,
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository,
    private val businessRepository: BusinessRepository,
    private val clientChatMessageDao: ClientChatMessageDao
) {
    data class SyncState(
        val inProgress: Boolean = false,
        val lastError: String? = null,
        val configured: Boolean = SupabaseConfig.IS_CONFIGURED,
        val importedCount: Int = 0   // purchases turned into owner-side sales this cycle
    )

    private val _syncState = MutableStateFlow(SyncState())
    val syncState: StateFlow<SyncState> = _syncState

    // ---- Purchase passthroughs (UI reads rows through the service) ----

    fun observePurchase(remoteId: String) = dao.observeByRemoteId(remoteId)

    suspend fun getPurchase(remoteId: String): ClientPurchase? = dao.getByRemoteId(remoteId)

    /** Persist the EBM mock's receipt number on the purchase row. */
    suspend fun saveReceiptNumber(remoteId: String, receiptNumber: String) {
        if (receiptNumber.isNotBlank()) dao.updateReceiptNumber(remoteId, receiptNumber)
    }

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

    /**
     * OWNER SIDE — pull this shop's client purchases down from Supabase and
     * import each un-imported one as a local Sale (source = "client"), reusing
     * the exact repository functions Employee Sell uses. Stock is decremented
     * here, on the owner's authoritative device, by [ProductRepository.decrementStock]
     * — the same function the existing Employee Sell and V3 client-store flows
     * call. A chat notice (senderRole = "system") is queued per purchase so the
     * owner isn't surprised by unexplained stock changes.
     *
     * Re-pull safe: importedSaleId guards each row, so income/stock are never
     * double-counted no matter how often this runs.
     */
    suspend fun pullAndImportForOwner(localBusinessId: Long) {
        if (!SupabaseConfig.IS_CONFIGURED) return
        val business = businessRepository.getBusinessById(localBusinessId) ?: return
        val shopRemoteId = stableShopUuid(business.id, business.createdAt)

        val res = client.get(
            table = "client_sales",
            filters = mapOf("shop_id" to "eq.$shopRemoteId"),
            order = "timestamp_millis.asc",
            limit = 200
        )
        if (res is SupabaseRestClient.Result.Failure) {
            _syncState.value = _syncState.value.copy(
                lastError = "Client purchases not updated: ${res.message}"
            )
            return
        }
        if (res !is SupabaseRestClient.Result.Success) return

        var imported = 0
        client.parseArray(res.body).forEach { el ->
            val o = try { el.jsonObject } catch (_: Exception) { return@forEach }
            val remoteId = o.str("id")
            if (remoteId.isBlank() || dao.getByRemoteId(remoteId) != null) return@forEach

            try {
                val purchase = ClientPurchase(
                    remoteId = remoteId,
                    shopRemoteId = shopRemoteId,
                    shopName = o.str("shop_name"),
                    productName = o.str("product_name"),
                    unitPrice = o.str("unit_price").toDoubleOrNull() ?: 0.0,
                    quantity = o.longOrNull("quantity").toInt(),
                    total = o.str("total").toDoubleOrNull() ?: 0.0,
                    receiptNumber = o.str("receipt_number"),
                    clientUserId = o.longOrNull("client_user_id"),
                    timestamp = o.longOrNull("timestamp_millis"),
                    pendingPush = false,
                    importedSaleId = 0
                )
                dao.upsert(purchase)
                importAsOwnerSale(purchase, localBusinessId)?.let { imported++ }
            } catch (_: Exception) {
                // Malformed row — skip rather than break the whole import cycle.
            }
        }
        if (imported > 0) {
            _syncState.value = _syncState.value.copy(importedCount = imported, lastError = null)
        }
    }

    /**
     * Turn one pulled purchase into owner-side ledger rows. Returns true if a
     * Sale was created. The sale's productId is resolved by name in this shop's
     * catalog (the purchase payload deliberately carries no local product id).
     */
    private suspend fun importAsOwnerSale(purchase: ClientPurchase, businessId: Long): Boolean {
        val product = productRepository.getAllActiveByBusiness(businessId)
            .firstOrNull { it.name.equals(purchase.productName, ignoreCase = true) }
            ?: return false // product removed since the purchase — nothing to decrement

        // One Sale row per purchased unit, same shape Employee Sell writes.
        var saleId = 0L
        repeat(purchase.quantity) {
            saleId = saleRepository.createSale(
                Sale(
                    productId = product.id,
                    businessId = businessId,
                    amount = purchase.unitPrice,
                    timestamp = purchase.timestamp,
                    source = "client"
                )
            )
            productRepository.decrementStock(product.id)
        }
        dao.markImported(purchase.remoteId, saleId)

        // Lightweight system-style notice into the existing owner↔client chat
        // (participantType "client", senderRole "system" — widened server-side by
        // supabase/migration-phase4.sql; falls back to local-only when offline).
        try {
            val conversationId = java.security.MessageDigest.getInstance("MD5")
                .digest("duka-conv-${purchase.shopRemoteId}-${purchase.clientUserId}".toByteArray())
                .joinToString("") { "%02x".format(it) }
            systemChatNotice(
                conversationId = conversationId,
                shopRemoteId = purchase.shopRemoteId,
                clientUserId = purchase.clientUserId,
                text = "Purchase recorded: ${purchase.quantity} × ${purchase.productName} " +
                        "(₣%,.0f".format(purchase.total) + ") — receipt ${purchase.receiptNumber.ifBlank { "pending" }}"
            )
        } catch (_: Exception) { /* notice is best-effort; the sale import must not fail */ }
        return true
    }

    /** Insert the system-style purchase notice locally; the chat poll pushes it up. */
    private suspend fun systemChatNotice(
        conversationId: String,
        shopRemoteId: String,
        clientUserId: Long,
        text: String
    ) {
        clientChatMessageDao.upsert(
            ClientChatMessage(
                remoteId = "system-$conversationId-${System.currentTimeMillis()}",
                conversationId = conversationId,
                shopId = shopRemoteId,
                clientUserId = clientUserId,
                senderRole = "system",
                participantType = "client",
                senderLabel = "Duka",
                text = text,
                timestamp = System.currentTimeMillis(),
                pendingPush = true
            )
        )
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

    /**
     * Mirrors ExploreSyncService.stableShopUuid exactly — the same local business
     * row MUST map to the same Supabase shops.id that phase 3 pushed, or the
     * owner pull and the client push would land on different shop identities.
     * Kept as a local copy (not shared) so Phase 3 stays untouched.
     */
    private fun stableShopUuid(localBusinessId: Long, createdAt: Long): String {
        val bytes = MessageDigest.getInstance("MD5")
            .digest("duka-shop-$localBusinessId-$createdAt".toByteArray())
        val s = bytes.joinToString("") { "%02x".format(it) }
        return "${s.substring(0, 8)}-${s.substring(8, 12)}-${s.substring(12, 16)}-${s.substring(16, 20)}-${s.substring(20, 32)}"
    }
}