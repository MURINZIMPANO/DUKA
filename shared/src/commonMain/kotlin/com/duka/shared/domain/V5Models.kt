package com.duka.shared.domain

import kotlinx.serialization.Serializable

/**
 * V5 Entity additions — Product Management, Stock Intelligence, Notifications, Analytics.
 * Pure Kotlin data classes for KMM.
 */
@Serializable
data class StockAdjustment(
    val id: Long = 0,
    val productId: Long,
    val businessId: Long,
    val delta: Int,
    val reason: String,
    val timestamp: Long = currentTimeMillis()
)

@Serializable
data class ProductAlert(
    val id: Long = 0,
    val businessId: Long,
    val productId: Long,
    val type: String,
    val message: String,
    val severity: String,
    val isRead: Boolean = false,
    val createdAt: Long = currentTimeMillis()
)

@Serializable
data class AppNotification(
    val id: Long = 0,
    val userId: Long,
    val businessId: Long,
    val type: String,
    val title: String,
    val body: String,
    val actionRoute: String,
    val isRead: Boolean = false,
    val createdAt: Long = currentTimeMillis()
)

@Serializable
data class Expense(
    val id: Long = 0,
    val businessId: Long,
    val label: String,
    val amount: Long,
    val periodStart: Long,
    val periodEnd: Long,
    val createdAt: Long = currentTimeMillis()
)
