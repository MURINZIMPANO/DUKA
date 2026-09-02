package com.duka.app.data.local.dao

import androidx.room.*
import com.duka.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * V3/V4 DAOs — Marketplace + Insight + Supply Network.
 *
 * SCOPE-GUARD: These DAOs only operate on V3/V4 tables. They do NOT write to V1/V2 tables.
 * Read-only access to V1/V2 data goes through existing V1/V2 DAOs/Repositories.
 *
 * MOCK STATUS: All queries operate on locally-seeded data. No real backend or external system.
 */

// --- Client-facing DAOs ---

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insert(purchase: Purchase): Long

    @Query("SELECT * FROM purchases WHERE clientUserId = :clientUserId ORDER BY timestamp DESC")
    fun getByClient(clientUserId: Long): Flow<List<Purchase>>

    @Query("SELECT SUM(amount) FROM purchases WHERE clientUserId = :clientUserId AND timestamp >= :startTime AND timestamp < :endTime")
    suspend fun getTotalSpentInRange(clientUserId: Long, startTime: Long, endTime: Long): Double?

    @Query("SELECT SUM(amount) FROM purchases WHERE clientUserId = :clientUserId")
    suspend fun getTotalAllTime(clientUserId: Long): Double?

    @Query("""
        SELECT p.category, SUM(pu.amount) as total
        FROM purchases pu
        INNER JOIN products p ON pu.productId = p.id
        WHERE pu.clientUserId = :clientUserId
          AND pu.timestamp BETWEEN :from AND :to
        GROUP BY p.category
    """)
    suspend fun spendingByCategory(clientUserId: Long, from: Long, to: Long): List<CategoryRevenue>

    @Query("""
        SELECT businessId, SUM(amount) as total
        FROM purchases
        WHERE clientUserId = :clientUserId
          AND timestamp BETWEEN :from AND :to
        GROUP BY businessId
    """)
    suspend fun spendingByBusiness(clientUserId: Long, from: Long, to: Long): List<BusinessSpend>
}

data class BusinessSpend(
    val businessId: Long,
    val total: Double
)

@Dao
interface BudgetGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(budgetGoal: BudgetGoal)

    @Query("SELECT * FROM budget_goals WHERE clientUserId = :clientUserId LIMIT 1")
    fun getForClient(clientUserId: Long): Flow<BudgetGoal?>

    @Query("SELECT * FROM budget_goals WHERE clientUserId = :clientUserId LIMIT 1")
    suspend fun getForClientOnce(clientUserId: Long): BudgetGoal?
}

@Dao
interface FeedbackReportDao {
    @Insert
    suspend fun insert(report: FeedbackReport): Long

    @Query("SELECT * FROM feedback_reports WHERE clientUserId = :clientUserId ORDER BY createdAt DESC")
    fun getByClient(clientUserId: Long): Flow<List<FeedbackReport>>

    @Query("SELECT * FROM feedback_reports ORDER BY createdAt DESC")
    fun getAll(): Flow<List<FeedbackReport>>
}

@Dao
interface ShopRatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rating: ShopRating)

    @Query("SELECT * FROM shop_ratings WHERE businessId = :businessId")
    suspend fun getByBusiness(businessId: Long): ShopRating?

    @Query("SELECT * FROM shop_ratings ORDER BY averageRating DESC")
    fun getAllRanked(): Flow<List<ShopRating>>
}

// --- V4 Supply Network DAOs ---

@Dao
interface WholesalerDao {
    @Insert
    suspend fun insert(wholesaler: Wholesaler): Long

    @Query("SELECT * FROM wholesalers ORDER BY rating DESC")
    fun getAll(): Flow<List<Wholesaler>>

    @Query("SELECT * FROM wholesalers WHERE name LIKE '%' || :query || '%' OR specialty LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<Wholesaler>>

    @Query("SELECT * FROM wholesalers WHERE id = :id")
    suspend fun getById(id: Long): Wholesaler?
}

@Dao
interface RestockRequestDao {
    @Insert
    suspend fun insert(request: RestockRequest): Long

    @Query("SELECT * FROM restock_requests WHERE businessId = :businessId ORDER BY requestedAt DESC")
    fun getByBusiness(businessId: Long): Flow<List<RestockRequest>>

    @Query("UPDATE restock_requests SET status = :status WHERE id = :requestId")
    suspend fun updateStatus(requestId: Long, status: String)
}
