package com.duka.app.data.repository

import com.duka.app.data.local.dao.*
import com.duka.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * V5 Repositories — Product Management, Stock Intelligence, Notifications, Analytics.
 */

@Singleton
class StockAdjustmentRepository @Inject constructor(
    private val stockAdjustmentDao: StockAdjustmentDao
) {
    suspend fun create(adjustment: StockAdjustment): Long = stockAdjustmentDao.insert(adjustment)
    fun getByBusiness(businessId: Long): Flow<List<StockAdjustment>> =
        stockAdjustmentDao.getByBusiness(businessId)
    fun getByProduct(productId: Long): Flow<List<StockAdjustment>> =
        stockAdjustmentDao.getByProduct(productId)
    suspend fun getUnitsSoldInRange(productId: Long, startTime: Long, endTime: Long): Int =
        stockAdjustmentDao.getUnitsSoldInRange(productId, startTime, endTime) ?: 0
}

@Singleton
class ProductAlertRepository @Inject constructor(
    private val productAlertDao: ProductAlertDao
) {
    suspend fun create(alert: ProductAlert): Long = productAlertDao.insert(alert)
    suspend fun createAll(alerts: List<ProductAlert>) = productAlertDao.insertAll(alerts)
    fun getByBusiness(businessId: Long): Flow<List<ProductAlert>> =
        productAlertDao.getByBusiness(businessId)
    fun getUnreadByBusiness(businessId: Long): Flow<List<ProductAlert>> =
        productAlertDao.getUnreadByBusiness(businessId)
    fun getUnreadCount(businessId: Long): Flow<Int> =
        productAlertDao.getUnreadCount(businessId)
    fun getUnreadByProduct(businessId: Long, productId: Long): Flow<List<ProductAlert>> =
        productAlertDao.getUnreadByProduct(businessId, productId)
    suspend fun getUnreadByProductOnce(businessId: Long, productId: Long): ProductAlert? =
        productAlertDao.getUnreadByProductOnce(businessId, productId)
    suspend fun markAsRead(alertId: Long) = productAlertDao.markAsRead(alertId)
    suspend fun markAllAsRead(businessId: Long) = productAlertDao.markAllAsRead(businessId)
    suspend fun deleteById(alertId: Long) = productAlertDao.deleteById(alertId)
}

@Singleton
class AppNotificationRepository @Inject constructor(
    private val appNotificationDao: AppNotificationDao
) {
    suspend fun create(notification: AppNotification): Long = appNotificationDao.insert(notification)
    fun getByUser(userId: Long): Flow<List<AppNotification>> =
        appNotificationDao.getByUser(userId)
    fun getUnreadByUser(userId: Long): Flow<List<AppNotification>> =
        appNotificationDao.getUnreadByUser(userId)
    fun getUnreadCount(userId: Long): Flow<Int> =
        appNotificationDao.getUnreadCount(userId)
    fun getByUserAndTypes(userId: Long, types: List<String>): Flow<List<AppNotification>> =
        appNotificationDao.getByUserAndTypes(userId, types)
    suspend fun markAsRead(notificationId: Long) = appNotificationDao.markAsRead(notificationId)
    suspend fun markAllAsRead(userId: Long) = appNotificationDao.markAllAsRead(userId)
    suspend fun deleteById(notificationId: Long) = appNotificationDao.deleteById(notificationId)
}

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    suspend fun create(expense: Expense): Long = expenseDao.insert(expense)
    fun getByBusiness(businessId: Long): Flow<List<Expense>> = expenseDao.getByBusiness(businessId)
    suspend fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Long =
        expenseDao.getTotalInRange(businessId, startTime, endTime) ?: 0L
    suspend fun deleteById(expenseId: Long) = expenseDao.deleteById(expenseId)
}
