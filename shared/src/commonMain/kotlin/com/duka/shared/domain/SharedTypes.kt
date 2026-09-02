package com.duka.shared.domain

import kotlinx.serialization.Serializable

/**
 * Utility data classes used across repositories.
 */
@Serializable
data class ProductCount(
    val productId: Long,
    val cnt: Int
)

@Serializable
data class CategoryRevenue(
    val category: String,
    val total: Double
)

@Serializable
data class SourceRevenue(
    val source: String,
    val total: Double
)

@Serializable
data class BusinessSpend(
    val businessId: Long,
    val total: Double
)

expect fun currentTimeMillis(): Long
