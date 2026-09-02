package com.duka.app.data.repository

import com.duka.app.data.local.dao.*
import com.duka.app.data.local.entity.*
import com.duka.app.data.local.dao.BusinessSpend
import com.duka.app.data.local.dao.CategoryRevenue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * V3/V4 Repositories — Marketplace + Insight + Supply Network.
 *
 * SCOPE-GUARD: These repositories only wrap V3/V4 DAOs. They do NOT write to V1/V2 tables.
 * Read access to V1/V2 data (Sale, Product, Business) goes through existing V1/V2 repositories.
 *
 * MOCK STATUS: All data is local-mock. No real payments, maps APIs, government systems,
 * or wholesaler partnerships are involved.
 */

// --- V3 Client repositories ---

@Singleton
class PurchaseRepository @Inject constructor(
    private val purchaseDao: PurchaseDao
) {
    suspend fun createPurchase(purchase: Purchase): Long = purchaseDao.insert(purchase)
    fun getByClient(clientUserId: Long): Flow<List<Purchase>> =
        purchaseDao.getByClient(clientUserId)
    suspend fun getTotalSpentInRange(clientUserId: Long, startTime: Long, endTime: Long): Double? =
        purchaseDao.getTotalSpentInRange(clientUserId, startTime, endTime)
    suspend fun getTotalAllTime(clientUserId: Long): Double? =
        purchaseDao.getTotalAllTime(clientUserId)

    suspend fun spendingByCategory(clientUserId: Long, from: Long, to: Long): List<CategoryRevenue> =
        purchaseDao.spendingByCategory(clientUserId, from, to)

    suspend fun spendingByBusiness(clientUserId: Long, from: Long, to: Long): List<BusinessSpend> =
        purchaseDao.spendingByBusiness(clientUserId, from, to)
}

@Singleton
class BudgetGoalRepository @Inject constructor(
    private val budgetGoalDao: BudgetGoalDao
) {
    suspend fun upsert(budgetGoal: BudgetGoal) = budgetGoalDao.upsert(budgetGoal)
    fun getForClient(clientUserId: Long): Flow<BudgetGoal?> =
        budgetGoalDao.getForClient(clientUserId)
    suspend fun getForClientOnce(clientUserId: Long): BudgetGoal? =
        budgetGoalDao.getForClientOnce(clientUserId)
}

@Singleton
class FeedbackReportRepository @Inject constructor(
    private val feedbackReportDao: FeedbackReportDao
) {
    suspend fun submitReport(report: FeedbackReport): Long = feedbackReportDao.insert(report)
    fun getByClient(clientUserId: Long): Flow<List<FeedbackReport>> =
        feedbackReportDao.getByClient(clientUserId)
    fun getAll(): Flow<List<FeedbackReport>> = feedbackReportDao.getAll()
}

@Singleton
class ShopRatingRepository @Inject constructor(
    private val shopRatingDao: ShopRatingDao
) {
    suspend fun upsert(rating: ShopRating) = shopRatingDao.upsert(rating)
    suspend fun getByBusiness(businessId: Long): ShopRating? =
        shopRatingDao.getByBusiness(businessId)
    fun getAllRanked(): Flow<List<ShopRating>> = shopRatingDao.getAllRanked()
}

// --- V4 Supply Network repositories ---

@Singleton
class WholesalerRepository @Inject constructor(
    private val wholesalerDao: WholesalerDao
) {
    suspend fun addWholesaler(wholesaler: Wholesaler): Long = wholesalerDao.insert(wholesaler)
    fun getAll(): Flow<List<Wholesaler>> = wholesalerDao.getAll()
    fun search(query: String): Flow<List<Wholesaler>> = wholesalerDao.search(query)
    suspend fun getById(id: Long): Wholesaler? = wholesalerDao.getById(id)
}

@Singleton
class RestockRequestRepository @Inject constructor(
    private val restockRequestDao: RestockRequestDao
) {
    suspend fun createRequest(request: RestockRequest): Long = restockRequestDao.insert(request)
    fun getByBusiness(businessId: Long): Flow<List<RestockRequest>> =
        restockRequestDao.getByBusiness(businessId)
    suspend fun updateStatus(requestId: Long, status: String) =
        restockRequestDao.updateStatus(requestId, status)
}
