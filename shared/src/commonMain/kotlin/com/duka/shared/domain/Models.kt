package com.duka.shared.domain

import kotlinx.serialization.Serializable

/**
 * Core domain models for Duka — shared between Android and iOS.
 * Pure Kotlin data classes with no platform-specific annotations.
 */
@Serializable
data class Business(
    val id: Long = 0,
    val name: String,
    val type: String,
    val employeeCount: Int,
    val language: String,
    val district: String,
    val createdAt: Long = currentTimeMillis()
)

@Serializable
data class Product(
    val id: Long = 0,
    val businessId: Long,
    val name: String,
    val price: Double,
    val stockQuantity: Int,
    val category: String,
    val createdAt: Long = currentTimeMillis(),
    val isDeleted: Boolean = false,
    val costPrice: Long = 0,
    val lowStockThreshold: Int = 5
)

@Serializable
data class Sale(
    val id: Long = 0,
    val productId: Long,
    val businessId: Long,
    val amount: Double,
    val timestamp: Long = currentTimeMillis(),
    val source: String
)

@Serializable
data class Employee(
    val id: Long = 0,
    val businessId: Long,
    val userId: Long = 0,
    val code: String,
    val name: String,
    val role: String = "cashier",
    val isActive: Boolean = true,
    val joinedAt: Long = currentTimeMillis(),
    val lastSeenAt: Long? = null
)

@Serializable
data class ChatMessage(
    val id: Long = 0,
    val businessId: Long,
    val senderRole: String,
    val senderLabel: String,
    val text: String,
    val timestamp: Long = currentTimeMillis()
)

@Serializable
data class Promo(
    val id: Long = 0,
    val businessId: Long,
    val title: String,
    val discountPercent: Int,
    val startDate: Long,
    val endDate: Long,
    val isLive: Boolean = true
)

@Serializable
data class IssueReport(
    val id: Long = 0,
    val businessId: Long,
    val category: String,
    val message: String,
    val attachBusinessId: Boolean,
    val status: String = "Under review",
    val createdAt: Long = currentTimeMillis()
)

@Serializable
data class EbmReceipt(
    val id: Long = 0,
    val saleId: Long,
    val receiptNumber: String,
    val status: String,
    val sentAt: Long = currentTimeMillis()
)

@Serializable
data class User(
    val id: Long = 0,
    val businessId: Long? = null,
    val fullName: String,
    val phoneOrEmail: String,
    val passwordHash: String,
    val role: String,
    val isDeleted: Boolean = false,
    val createdAt: Long = currentTimeMillis()
)

@Serializable
data class TaxProfile(
    val businessId: Long,
    val quarterlyTurnover: Double,
    val tier: String,
    val vatCollected: Double
)
