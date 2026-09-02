package com.duka.android.data.repository

import com.duka.app.data.local.dao.AppNotificationDao
import com.duka.app.data.local.dao.ExpenseDao
import com.duka.app.data.local.dao.ProductAlertDao
import com.duka.app.data.local.dao.StockAdjustmentDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.AppNotificationRepository
import com.duka.shared.data.repository.ExpenseRepository
import com.duka.shared.data.repository.ProductAlertRepository
import com.duka.shared.data.repository.StockAdjustmentRepository
import com.duka.shared.domain.AppNotification
import com.duka.shared.domain.Expense
import com.duka.shared.domain.ProductAlert
import com.duka.shared.domain.StockAdjustment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// ── StockAdjustmentRepository ──

@Singleton
class AndroidStockAdjustmentRepository @Inject constructor(
    private val stockAdjustmentDao: StockAdjustmentDao
) : StockAdjustmentRepository {

    override suspend fun create(adjustment: StockAdjustment): Long =
        stockAdjustmentDao.insert(adjustment.toRoom())

    override fun getByBusiness(businessId: Long): Flow<List<StockAdjustment>> =
        stockAdjustmentDao.getByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override fun getByProduct(productId: Long): Flow<List<StockAdjustment>> =
        stockAdjustmentDao.getByProduct(productId).map { list -> list.map { it.toShared() } }

    override suspend fun getUnitsSoldInRange(productId: Long, startTime: Long, endTime: Long): Int =
        stockAdjustmentDao.getUnitsSoldInRange(productId, startTime, endTime) ?: 0
}

// ── ProductAlertRepository ──

@Singleton
class AndroidProductAlertRepository @Inject constructor(
    private val productAlertDao: ProductAlertDao
) : ProductAlertRepository {

    override suspend fun create(alert: ProductAlert): Long =
        productAlertDao.insert(alert.toRoom())

    override suspend fun createAll(alerts: List<ProductAlert>) =
        productAlertDao.insertAll(alerts.map { it.toRoom() })

    override fun getByBusiness(businessId: Long): Flow<List<ProductAlert>> =
        productAlertDao.getByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override fun getUnreadByBusiness(businessId: Long): Flow<List<ProductAlert>> =
        productAlertDao.getUnreadByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override fun getUnreadCount(businessId: Long): Flow<Int> =
        productAlertDao.getUnreadCount(businessId)

    override fun getUnreadByProduct(businessId: Long, productId: Long): Flow<List<ProductAlert>> =
        productAlertDao.getUnreadByProduct(businessId, productId).map { list -> list.map { it.toShared() } }

    override suspend fun getUnreadByProductOnce(businessId: Long, productId: Long): ProductAlert? =
        productAlertDao.getUnreadByProductOnce(businessId, productId)?.toShared()

    override suspend fun markAsRead(alertId: Long) =
        productAlertDao.markAsRead(alertId)

    override suspend fun markAllAsRead(businessId: Long) =
        productAlertDao.markAllAsRead(businessId)

    override suspend fun deleteById(alertId: Long) =
        productAlertDao.deleteById(alertId)
}

// ── AppNotificationRepository ──

@Singleton
class AndroidAppNotificationRepository @Inject constructor(
    private val appNotificationDao: AppNotificationDao
) : AppNotificationRepository {

    override suspend fun create(notification: AppNotification): Long =
        appNotificationDao.insert(notification.toRoom())

    override fun getByUser(userId: Long): Flow<List<AppNotification>> =
        appNotificationDao.getByUser(userId).map { list -> list.map { it.toShared() } }

    override fun getUnreadByUser(userId: Long): Flow<List<AppNotification>> =
        appNotificationDao.getUnreadByUser(userId).map { list -> list.map { it.toShared() } }

    override fun getUnreadCount(userId: Long): Flow<Int> =
        appNotificationDao.getUnreadCount(userId)

    override fun getByUserAndTypes(userId: Long, types: List<String>): Flow<List<AppNotification>> =
        appNotificationDao.getByUserAndTypes(userId, types).map { list -> list.map { it.toShared() } }

    override suspend fun markAsRead(notificationId: Long) =
        appNotificationDao.markAsRead(notificationId)

    override suspend fun markAllAsRead(userId: Long) =
        appNotificationDao.markAllAsRead(userId)

    override suspend fun deleteById(notificationId: Long) =
        appNotificationDao.deleteById(notificationId)
}

// ── ExpenseRepository ──

@Singleton
class AndroidExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override suspend fun create(expense: Expense): Long =
        expenseDao.insert(expense.toRoom())

    override fun getByBusiness(businessId: Long): Flow<List<Expense>> =
        expenseDao.getByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override suspend fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Long =
        expenseDao.getTotalInRange(businessId, startTime, endTime) ?: 0L

    override suspend fun deleteById(expenseId: Long) =
        expenseDao.deleteById(expenseId)
}
