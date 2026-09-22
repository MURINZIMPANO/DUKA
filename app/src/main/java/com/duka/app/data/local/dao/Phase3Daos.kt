package com.duka.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duka.app.data.local.entity.ClientChatMessage
import com.duka.app.data.local.entity.ExploreShop
import kotlinx.coroutines.flow.Flow

/**
 * Phase 3 DAOs — Explore cache + client chat.
 *
 * SCOPE-GUARD: operate only on Phase 3 tables. No V1–V6 tables are touched.
 */

@Dao
interface ExploreShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(shops: List<ExploreShop>)

    @Query("SELECT * FROM explore_shops WHERE isFlagged = 0 ORDER BY name ASC")
    fun observeAll(): Flow<List<ExploreShop>>

    @Query("SELECT * FROM explore_shops WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getById(remoteId: String): ExploreShop?

    @Query("SELECT * FROM explore_shops WHERE remoteId = :remoteId LIMIT 1")
    fun observeById(remoteId: String): Flow<ExploreShop?>

    @Query(
        """
        SELECT * FROM explore_shops
        WHERE isFlagged = 0
          AND (:category = '' OR category = :category)
          AND (:district = '' OR district = :district)
          AND (:minRating = 0.0 OR ratingAverage >= :minRating)
          AND (:maxPrice = 0.0 OR avgPrice <= :maxPrice)
          AND (:query = '' OR name LIKE '%' || :query || '%')
        ORDER BY salesVelocity DESC
        """
    )
    fun search(
        query: String,
        category: String,
        district: String,
        minRating: Double,
        maxPrice: Double
    ): Flow<List<ExploreShop>>

    /** Newest registered shops for "New on Duka". */
    @Query("SELECT * FROM explore_shops WHERE isFlagged = 0 ORDER BY registeredAt DESC LIMIT 10")
    fun observeNewest(): Flow<List<ExploreShop>>

    @Query("SELECT COUNT(*) FROM explore_shops")
    suspend fun count(): Int

    @Query("SELECT MAX(syncedAt) FROM explore_shops")
    suspend fun lastSyncedAt(): Long?
}

@Dao
interface ClientChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(messages: List<ClientChatMessage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(message: ClientChatMessage)

    @Query(
        """
        SELECT * FROM client_chat_messages
        WHERE conversationId = :conversationId
        ORDER BY timestamp ASC
        """
    )
    fun observeConversation(conversationId: String): Flow<List<ClientChatMessage>>

    @Query(
        """
        SELECT * FROM client_chat_messages
        WHERE conversationId = :conversationId
        ORDER BY timestamp DESC
        LIMIT 1
        """
    )
    suspend fun lastMessage(conversationId: String): ClientChatMessage?

    /** Locally-pending sends awaiting push — the retry queue. */
    @Query(
        """
        SELECT * FROM client_chat_messages
        WHERE conversationId = :conversationId AND pendingPush = 1
        ORDER BY timestamp ASC
        """
    )
    suspend fun pendingFor(conversationId: String): List<ClientChatMessage>

    /** All conversations' messages, newest first — owner chat list derives groups from this. */
    @Query("SELECT * FROM client_chat_messages ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<ClientChatMessage>>

    @Query("UPDATE client_chat_messages SET pendingPush = 0, remoteId = :remoteId WHERE timestamp = :localTimestamp AND pendingPush = 1")
    suspend fun confirmPush(localTimestamp: Long, remoteId: String)

    @Query("DELETE FROM client_chat_messages")
    suspend fun clearForTests()
}
