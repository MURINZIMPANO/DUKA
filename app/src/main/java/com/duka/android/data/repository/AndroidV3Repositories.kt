package com.duka.android.data.repository

import com.duka.app.data.local.dao.BudgetGoalDao
import com.duka.app.data.local.dao.FeedbackReportDao
import com.duka.app.data.local.dao.PurchaseDao
import com.duka.app.data.local.dao.RestockRequestDao
import com.duka.app.data.local.dao.ShopRatingDao
import com.duka.app.data.local.dao.WholesalerDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.BudgetGoalRepository
import com.duka.shared.data.repository.FeedbackReportRepository
import com.duka.shared.data.repository.PurchaseRepository
import com.duka.shared.data.repository.RestockRequestRepository
import com.duka.shared.data.repository.ShopRatingRepository
import com.duka.shared.data.repository.WholesalerRepository
import com.duka.shared.domain.BudgetGoal
import com.duka.shared.domain.CategoryRevenue
import com.duka.shared.domain.FeedbackReport
import com.duka.shared.domain.Purchase
import com.duka.shared.domain.RestockRequest
import com.duka.shared.domain.ShopRating
import com.duka.shared.domain.Wholesaler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// ── PurchaseRepository ──

@Singleton
class AndroidPurchaseRepository @Inject constructor(
    private val purchaseDao: PurchaseDao
) : PurchaseRepository {

    override suspend fun createPurchase(purchase: Purchase): Long =
        purchaseDao.insert(purchase.toRoom())

    override fun getByClient(clientUserId: Long): Flow<List<Purchase>> =
        purchaseDao.getByClient(clientUserId).map { list -> list.map { it.toShared() } }

    override suspend fun getTotalSpentInRange(clientUserId: Long, startTime: Long, endTime: Long): Double? =
        purchaseDao.getTotalSpentInRange(clientUserId, startTime, endTime)

    override suspend fun getTotalAllTime(clientUserId: Long): Double? =
        purchaseDao.getTotalAllTime(clientUserId)

    override suspend fun spendingByCategory(clientUserId: Long, from: Long, to: Long): List<CategoryRevenue> =
        purchaseDao.spendingByCategory(clientUserId, from, to).map { it.toShared() }

    override suspend fun spendingByBusiness(clientUserId: Long, from: Long, to: Long): List<com.duka.shared.domain.BusinessSpend> =
        purchaseDao.spendingByBusiness(clientUserId, from, to).map { it.toShared() }
}

// ── BudgetGoalRepository ──

@Singleton
class AndroidBudgetGoalRepository @Inject constructor(
    private val budgetGoalDao: BudgetGoalDao
) : BudgetGoalRepository {

    override suspend fun upsert(budgetGoal: BudgetGoal) =
        budgetGoalDao.upsert(budgetGoal.toRoom())

    override fun getForClient(clientUserId: Long): Flow<BudgetGoal?> =
        budgetGoalDao.getForClient(clientUserId).map { it?.toShared() }

    override suspend fun getForClientOnce(clientUserId: Long): BudgetGoal? =
        budgetGoalDao.getForClientOnce(clientUserId)?.toShared()
}

// ── FeedbackReportRepository ──

@Singleton
class AndroidFeedbackReportRepository @Inject constructor(
    private val feedbackReportDao: FeedbackReportDao
) : FeedbackReportRepository {

    override suspend fun submitReport(report: FeedbackReport): Long =
        feedbackReportDao.insert(report.toRoom())

    override fun getByClient(clientUserId: Long): Flow<List<FeedbackReport>> =
        feedbackReportDao.getByClient(clientUserId).map { list -> list.map { it.toShared() } }

    override fun getAll(): Flow<List<FeedbackReport>> =
        feedbackReportDao.getAll().map { list -> list.map { it.toShared() } }
}

// ── ShopRatingRepository ──

@Singleton
class AndroidShopRatingRepository @Inject constructor(
    private val shopRatingDao: ShopRatingDao
) : ShopRatingRepository {

    override suspend fun upsert(rating: ShopRating) =
        shopRatingDao.upsert(rating.toRoom())

    override suspend fun getByBusiness(businessId: Long): ShopRating? =
        shopRatingDao.getByBusiness(businessId)?.toShared()

    override fun getAllRanked(): Flow<List<ShopRating>> =
        shopRatingDao.getAllRanked().map { list -> list.map { it.toShared() } }
}

// ── WholesalerRepository ──

@Singleton
class AndroidWholesalerRepository @Inject constructor(
    private val wholesalerDao: WholesalerDao
) : WholesalerRepository {

    override suspend fun addWholesaler(wholesaler: Wholesaler): Long =
        wholesalerDao.insert(wholesaler.toRoom())

    override fun getAll(): Flow<List<Wholesaler>> =
        wholesalerDao.getAll().map { list -> list.map { it.toShared() } }

    override fun search(query: String): Flow<List<Wholesaler>> =
        wholesalerDao.search(query).map { list -> list.map { it.toShared() } }

    override suspend fun getById(id: Long): Wholesaler? =
        wholesalerDao.getById(id)?.toShared()
}

// ── RestockRequestRepository ──

@Singleton
class AndroidRestockRequestRepository @Inject constructor(
    private val restockRequestDao: RestockRequestDao
) : RestockRequestRepository {

    override suspend fun createRequest(request: RestockRequest): Long =
        restockRequestDao.insert(request.toRoom())

    override fun getByBusiness(businessId: Long): Flow<List<RestockRequest>> =
        restockRequestDao.getByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override suspend fun updateStatus(requestId: Long, status: String) =
        restockRequestDao.updateStatus(requestId, status)
}
