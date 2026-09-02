package com.duka.shared.domain

import kotlinx.serialization.Serializable

/**
 * V3/V4 Entity additions — Marketplace + Insight + Supply Network.
 * Pure Kotlin data classes for KMM.
 */
@Serializable
data class Purchase(
    val id: Long = 0,
    val clientUserId: Long,
    val businessId: Long,
    val productId: Long,
    val amount: Double,
    val timestamp: Long = currentTimeMillis(),
    val receiptNumber: String = ""
)

@Serializable
data class BudgetGoal(
    val id: Long = 0,
    val clientUserId: Long,
    val weeklyLimit: Double,
    val createdAt: Long = currentTimeMillis()
)

@Serializable
data class FeedbackReport(
    val id: Long = 0,
    val clientUserId: Long,
    val targetBusinessId: Long,
    val category: String,
    val message: String,
    val isAnonymous: Boolean = false,
    val status: String = "Under review",
    val createdAt: Long = currentTimeMillis()
)

@Serializable
data class ShopRating(
    val id: Long = 0,
    val businessId: Long,
    val averageRating: Double,
    val ratingCount: Int
)

@Serializable
data class Wholesaler(
    val id: Long = 0,
    val name: String,
    val specialty: String,
    val rating: Double,
    val deliveryEstimate: String
)

@Serializable
data class RestockRequest(
    val id: Long = 0,
    val businessId: Long,
    val productId: Long,
    val wholesalerId: Long,
    val quantity: Int,
    val status: String = "requested",
    val requestedAt: Long = currentTimeMillis()
)
