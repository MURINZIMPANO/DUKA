package com.duka.phase3.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.duka.phase3.sync.SupabaseRestClient
import com.duka.phase3.sync.str
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Phase 3 — Cached public catalog rows for other shops' Shop Profile screens.
 * Kept separately from the owner's own V1/V2 products table (read-mostly cache).
 */
@Entity(tableName = "remote_shop_products")
data class RemoteShopProduct(
    @PrimaryKey val remoteId: String,
    val shopId: String,
    val name: String,
    val price: Double,
    val category: String
)

@Dao
interface RemoteShopProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(products: List<RemoteShopProduct>)

    @Query("SELECT * FROM remote_shop_products WHERE shopId = :shopId ORDER BY name ASC")
    fun observeByShop(shopId: String): Flow<List<RemoteShopProduct>>
}

/**
 * Phase 3 — read/write facade over Explore + remote catalog + client chat.
 * Everything flows through the local Room cache so Explore keeps working offline;
 * refresh() best-effort syncs with Supabase and reports failures visibly.
 */
@Singleton
class Phase3Repository @Inject constructor(
    val exploreShopDao: com.duka.app.data.local.dao.ExploreShopDao,
    private val remoteProductDao: RemoteShopProductDao,
    private val clientChatMessageDao: com.duka.app.data.local.dao.ClientChatMessageDao,
    private val syncService: com.duka.phase3.sync.ExploreSyncService,
    private val client: SupabaseRestClient
) {
    val syncState get() = syncService.syncState

    fun observeAllShops(): Flow<List<com.duka.app.data.local.entity.ExploreShop>> =
        exploreShopDao.observeAll()

    fun searchShops(
        query: String,
        category: String,
        district: String,
        minRating: Double,
        maxPrice: Double
    ): Flow<List<com.duka.app.data.local.entity.ExploreShop>> =
        exploreShopDao.search(query, category, district, minRating, maxPrice)

    fun observeNewest(): Flow<List<com.duka.app.data.local.entity.ExploreShop>> =
        exploreShopDao.observeNewest()

    fun observeShop(shopRemoteId: String): Flow<com.duka.app.data.local.entity.ExploreShop?> =
        exploreShopDao.observeById(shopRemoteId)

    fun observeShopProducts(shopRemoteId: String): Flow<List<RemoteShopProduct>> =
        remoteProductDao.observeByShop(shopRemoteId)

    suspend fun shopById(shopRemoteId: String) = exploreShopDao.getById(shopRemoteId)

    /** Push own shop up + pull the directory down; failures visible via syncState. */
    suspend fun refresh(district: String) = syncService.syncNow(district)

    /** Pull one shop's public catalog for its profile page (best-effort, cached offline). */
    suspend fun refreshShopProducts(shopRemoteId: String) {
        if (!com.duka.phase3.sync.SupabaseConfig.IS_CONFIGURED) return
        val res = client.get(
            table = "shop_products",
            filters = mapOf("shop_id" to "eq.$shopRemoteId"),
            order = "name.asc",
            limit = 300
        )
        if (res is SupabaseRestClient.Result.Success) {
            val rows = client.parseArray(res.body).mapNotNull { el ->
                val o = try { el.jsonObject } catch (_: Exception) { return@mapNotNull null }
                try {
                    RemoteShopProduct(
                        remoteId = o.str("shop_id") + ":" + o.str("name"),
                        shopId = o.str("shop_id"),
                        name = o.str("name"),
                        price = o.str("price").toDoubleOrNull() ?: 0.0,
                        category = o.str("category")
                    )
                } catch (_: Exception) {
                    null
                }
            }
            if (rows.isNotEmpty()) remoteProductDao.upsertAll(rows)
        }
    }

    // ---- Chat passthroughs (see ClientChatService) ----
    fun observeConversation(conversationId: String) =
        clientChatMessageDao.observeConversation(conversationId)

    fun observeAllClientMessages(): Flow<List<com.duka.app.data.local.entity.ClientChatMessage>> =
        clientChatMessageDao.observeAll()

    suspend fun lastMessage(conversationId: String) =
        clientChatMessageDao.lastMessage(conversationId)
}
