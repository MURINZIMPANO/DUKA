package com.duka.phase4.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Phase 4 — Client purchase record for the cross-device purchase flow.
 *
 * One table, two roles (deliberate, mirrors the Phase 3 chat-table pattern):
 *  * CLIENT device: the row is created local-first when the client taps Buy.
 *    `pendingPush = true` until Supabase confirms it — this flag IS the visible
 *    offline/pending state ("Purchase pending — will complete when you're back
 *    online"). It is retried on every sync cycle, never silently dropped.
 *  * OWNER device: rows pulled down from Supabase land here too, acting as an
 *    import ledger. `importedSaleId` records the local Sale (source = "client")
 *    created from it, so a re-pull never duplicates income or stock changes.
 *
 * Only CLIENT-initiated purchases are stored here and synced. Owner/employee/
 * voice sales keep their existing local-only lifecycle (see Phase 3 data-
 * minimisation notes) — this table widens, not replaces, that decision.
 *
 * Identity: `remoteId` is a deterministic UUID (see ClientPurchaseSyncService)
 * so repeated pushes upsert the same server row instead of duplicating it.
 */
@Entity(tableName = "client_purchases")
data class ClientPurchase(
    @PrimaryKey val remoteId: String,
    val shopRemoteId: String,
    val shopName: String,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val total: Double,
    val receiptNumber: String,   // filled in by the EBM mock on the receipt screen
    val clientUserId: Long,
    val timestamp: Long,
    val pendingPush: Boolean,
    val importedSaleId: Long     // owner device only; 0 = not imported yet
)

@Dao
interface ClientPurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(purchase: ClientPurchase)

    @Query("SELECT * FROM client_purchases WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: String): ClientPurchase?

    @Query("SELECT * FROM client_purchases WHERE remoteId = :remoteId")
    fun observeByRemoteId(remoteId: String): Flow<ClientPurchase?>

    /** Locally-created purchases not yet confirmed by the backend. */
    @Query("SELECT * FROM client_purchases WHERE pendingPush = 1")
    suspend fun pendingAll(): List<ClientPurchase>

    @Query("UPDATE client_purchases SET pendingPush = 0 WHERE remoteId = :remoteId")
    suspend fun confirmPush(remoteId: String)

    @Query("UPDATE client_purchases SET receiptNumber = :receiptNumber WHERE remoteId = :remoteId")
    suspend fun updateReceiptNumber(remoteId: String, receiptNumber: String)

    @Query("UPDATE client_purchases SET importedSaleId = :saleId WHERE remoteId = :remoteId")
    suspend fun markImported(remoteId: String, saleId: Long)
}
