package com.duka.app.data.local.dao

import androidx.room.*
import com.duka.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

/**
 * V5 DAOs — Product Management, Stock Intelligence, Notifications, Analytics.
 */

// --- Stock Adjustment DAO ---

@Dao
interface StockAdjustmentDao {
    @Insert
    suspend fun insert(adjustment: StockAdjustment): Long

    @Query("SELECT * FROM stock_adjustments WHERE businessId = :businessId ORDER BY timestamp DESC")
    fun getByBusiness(businessId: Long): Flow<List<StockAdjustment>>

    @Query("SELECT * FROM stock_adjustments WHERE productId = :productId ORDER BY timestamp DESC")
    fun getByProduct(productId: Long): Flow<List<StockAdjustment>>

    @Query("SELECT * FROM stock_adjustments WHERE businessId = :businessId AND timestamp >= :startTime AND timestamp < :endTime")
    suspend fun getByBusinessInRange(businessId: Long, startTime: Long, endTime: Long): List<StockAdjustment>

    @Query("SELECT SUM(delta) FROM stock_adjustments WHERE productId = :productId AND reason = 'sale' AND timestamp >= :startTime AND timestamp < :endTime")
    suspend fun getUnitsSoldInRange(productId: Long, startTime: Long, endTime: Long): Int?
}

// --- Product Alert DAO ---

@Dao
interface ProductAlertDao {
    @Insert
    suspend fun insert(alert: ProductAlert): Long

    @Insert
    suspend fun insertAll(alerts: List<ProductAlert>)

    @Query("SELECT * FROM product_alerts WHERE businessId = :businessId ORDER BY createdAt DESC")
    fun getByBusiness(businessId: Long): Flow<List<ProductAlert>>

    @Query("SELECT * FROM product_alerts WHERE businessId = :businessId AND isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadByBusiness(businessId: Long): Flow<List<ProductAlert>>

    @Query("SELECT COUNT(*) FROM product_alerts WHERE businessId = :businessId AND isRead = 0")
    fun getUnreadCount(businessId: Long): Flow<Int>

    @Query("SELECT * FROM product_alerts WHERE businessId = :businessId AND productId = :productId AND isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadByProduct(businessId: Long, productId: Long): Flow<List<ProductAlert>>

    @Query("SELECT * FROM product_alerts WHERE businessId = :businessId AND productId = :productId AND isRead = 0 ORDER BY createdAt DESC LIMIT 1")
    suspend fun getUnreadByProductOnce(businessId: Long, productId: Long): ProductAlert?

    @Query("UPDATE product_alerts SET isRead = 1 WHERE id = :alertId")
    suspend fun markAsRead(alertId: Long)

    @Query("UPDATE product_alerts SET isRead = 1 WHERE businessId = :businessId")
    suspend fun markAllAsRead(businessId: Long)

    @Query("DELETE FROM product_alerts WHERE id = :alertId")
    suspend fun deleteById(alertId: Long)
}

// --- App Notification DAO ---

@Dao
interface AppNotificationDao {
    @Insert
    suspend fun insert(notification: AppNotification): Long

    @Query("SELECT * FROM app_notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: Long): Flow<List<AppNotification>>

    @Query("SELECT * FROM app_notifications WHERE userId = :userId AND isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadByUser(userId: Long): Flow<List<AppNotification>>

    @Query("SELECT COUNT(*) FROM app_notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadCount(userId: Long): Flow<Int>

    @Query("SELECT * FROM app_notifications WHERE userId = :userId AND type IN (:types) ORDER BY createdAt DESC")
    fun getByUserAndTypes(userId: Long, types: List<String>): Flow<List<AppNotification>>

    @Query("UPDATE app_notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Long)

    @Query("UPDATE app_notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: Long)

    @Query("DELETE FROM app_notifications WHERE id = :notificationId")
    suspend fun deleteById(notificationId: Long)
}

// --- Expense DAO ---

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense): Long

    @Query("SELECT * FROM expenses WHERE businessId = :businessId ORDER BY createdAt DESC")
    fun getByBusiness(businessId: Long): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE businessId = :businessId AND periodStart >= :startTime AND periodEnd <= :endTime")
    suspend fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Long?

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteById(expenseId: Long)
}
