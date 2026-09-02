package com.duka.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * V3/V4 Entity additions — Marketplace + Insight + Supply Network.
 *
 * SCOPE-GUARD: All entities in this file are additive. They do NOT modify existing V1/V2 entities.
 * New write paths only go into these new tables. Read paths into V1/V2 tables (Sale, Product, Business)
 * are read-only via existing Repository interfaces.
 *
 * MOCK STATUS: All V3/V4 data is local-mock. Purchase, BudgetGoal, FeedbackReport, ShopRating,
 * Wholesaler, and RestockRequest are all computed from or seeded alongside existing local seed data.
 * No real payments, maps APIs, government systems, or wholesaler partnerships are involved.
 */

// --- Client-facing entities ---

@Entity(tableName = "purchases")
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientUserId: Long,
    val businessId: Long,
    val productId: Long,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val receiptNumber: String = "" // from MockEbmGateway
)

@Entity(tableName = "budget_goals")
data class BudgetGoal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientUserId: Long,
    val weeklyLimit: Double,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "feedback_reports")
data class FeedbackReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientUserId: Long,
    val targetBusinessId: Long,
    val category: String,
    val message: String,
    val isAnonymous: Boolean = false,
    val status: String = "Under review",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "shop_ratings")
data class ShopRating(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long,
    val averageRating: Double,
    val ratingCount: Int
)

// --- V4 Supply Network entities ---

@Entity(tableName = "wholesalers")
data class Wholesaler(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val specialty: String,
    val rating: Double,
    val deliveryEstimate: String // e.g. "2-3 days"
)

@Entity(tableName = "restock_requests")
data class RestockRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long,
    val productId: Long,
    val wholesalerId: Long,
    val quantity: Int,
    val status: String = "requested", // "requested" | "in_transit" | "delivered"
    val requestedAt: Long = System.currentTimeMillis()
)
