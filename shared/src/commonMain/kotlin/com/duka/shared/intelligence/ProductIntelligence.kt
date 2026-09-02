package com.duka.shared.intelligence

import com.duka.shared.domain.Product
import com.duka.shared.domain.ProductAlert
import com.duka.shared.domain.Sale
import com.duka.shared.domain.currentTimeMillis

/**
 * Pure functions for product intelligence — no platform dependencies.
 */
object ProductIntelligence {

    fun checkLowStock(products: List<Product>, businessId: Long): List<ProductAlert> {
        val now = currentTimeMillis()
        return products.filter { it.stockQuantity <= it.lowStockThreshold }.map { product ->
            ProductAlert(
                businessId = businessId, productId = product.id, type = "low_stock",
                message = if (product.stockQuantity == 0) "${product.name} is out of stock" else "${product.name} is running low — ${product.stockQuantity} left",
                severity = if (product.stockQuantity == 0) "critical" else "warning",
                isRead = false, createdAt = now
            )
        }
    }

    fun checkSlowMovers(products: List<Product>, sales: List<Sale>, businessId: Long, windowDays: Int = 14): List<ProductAlert> {
        val now = currentTimeMillis()
        val windowStart = now - (windowDays * 24L * 60 * 60 * 1000)
        val salesByProduct = sales.groupBy { it.productId }
        return products.filter { product ->
            product.stockQuantity > 0 && product.createdAt <= windowStart &&
                (salesByProduct[product.id]?.none { it.timestamp >= windowStart } ?: true)
        }.map { product ->
            ProductAlert(businessId = businessId, productId = product.id, type = "slow_mover",
                message = "${product.name} hasn't sold in $windowDays days", severity = "info",
                isRead = false, createdAt = now)
        }
    }

    fun computeSalesVelocity(productId: Long, sales: List<Sale>, windowDays: Int = 7): Double {
        val now = currentTimeMillis()
        val windowStart = now - (windowDays * 24L * 60 * 60 * 1000)
        val unitsSold = sales.count { it.productId == productId && it.timestamp >= windowStart }
        return unitsSold.toDouble() / windowDays
    }

    fun checkRestockSuggestion(product: Product, velocity: Double): ProductAlert? {
        if (velocity <= 0.0) return null
        val daysRemaining = product.stockQuantity / velocity
        if (daysRemaining < 7) {
            return ProductAlert(businessId = product.businessId, productId = product.id, type = "restock_suggestion",
                message = "${product.name} will run out in ~${daysRemaining.toInt().coerceAtLeast(1)} days",
                severity = "warning", isRead = false, createdAt = currentTimeMillis())
        }
        return null
    }

    fun generateAllAlerts(products: List<Product>, sales: List<Sale>, businessId: Long): List<ProductAlert> {
        return checkLowStock(products, businessId) +
            checkSlowMovers(products, sales, businessId) +
            products.mapNotNull { checkRestockSuggestion(it, computeSalesVelocity(it.id, sales)) }
    }
}
