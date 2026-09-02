package com.duka.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "businesses")
data class Business(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val employeeCount: Int,
    val language: String,
    val district: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long,
    val name: String,
    val price: Double,
    val stockQuantity: Int,
    val category: String,
    val createdAt: Long = System.currentTimeMillis(),
    // V5 additions
    val isDeleted: Boolean = false,
    val costPrice: Long = 0,        // cost from supplier, 0 = not set
    val lowStockThreshold: Int = 5  // alert when stock drops below this
)

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val businessId: Long,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val source: String // "owner" | "employee" | "voice" | "client"
)

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long,
    val userId: Long = 0,          // NEW — links to the User row created when they join
    val code: String,              // 6-digit alphanumeric, unique per business
    val name: String,
    val role: String = "cashier", // "cashier" | "stockist" | "manager"
    val isActive: Boolean = true,  // false = deactivated, can no longer log in
    val joinedAt: Long = System.currentTimeMillis(),
    val lastSeenAt: Long? = null   // updated each time the employee logs in; null if never used
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long,
    val senderRole: String,
    val senderLabel: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "promos")
data class Promo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long,
    val title: String,
    val discountPercent: Int,
    val startDate: Long,
    val endDate: Long,
    val isLive: Boolean = true
)

@Entity(tableName = "issue_reports")
data class IssueReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long,
    val category: String,
    val message: String,
    val attachBusinessId: Boolean,
    val status: String = "Under review",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ebm_receipts")
data class EbmReceipt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val saleId: Long,
    val receiptNumber: String,
    val status: String, // "sent" | "pending"
    val sentAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessId: Long? = null, // null until a business is created/joined
    val fullName: String,
    val phoneOrEmail: String,
    val passwordHash: String,
    val role: String, // "owner" | "employee" | "client"
    val isDeleted: Boolean = false, // soft-delete flag for account deletion
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tax_profiles")
data class TaxProfile(
    @PrimaryKey val businessId: Long,
    val quarterlyTurnover: Double,
    val tier: String,
    val vatCollected: Double
)
