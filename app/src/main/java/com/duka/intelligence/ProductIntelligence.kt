package com.duka.intelligence

import com.duka.app.data.local.entity.Product
import com.duka.app.data.local.entity.ProductAlert
import com.duka.app.data.local.entity.Sale

/**
 * Pure functions for product intelligence — no Android dependencies, fully unit-testable.
 * All analysis is done here; the Worker and UI only consume results.
 */
object ProductIntelligence {

    /**
     * Check for low stock products.
     * For each product where stockQuantity <= lowStockThreshold: generate an alert.
     */
    fun checkLowStock(
        products: List<Product>,
        businessId: Long
    ): List<ProductAlert> {
        val now = System.currentTimeMillis()
        return products.filter { it.stockQuantity <= it.lowStockThreshold }.map { product ->
            ProductAlert(
                businessId = businessId,
                productId = product.id,
                type = "low_stock",
                message = if (product.stockQuantity == 0) {
                    "${product.name} is out of stock — restock now"
                } else {
                    "${product.name} is running low — ${product.stockQuantity} left"
                },
                severity = if (product.stockQuantity == 0) "critical" else "warning",
                isRead = false,
                createdAt = now
            )
        }
    }

    /**
     * Check for slow-moving products.
     * A product is a "slow mover" if it has had zero sales in the past [windowDays] days
     * AND stockQuantity > 0 AND has been in the system for at least [windowDays].
     */
    fun checkSlowMovers(
        products: List<Product>,
        sales: List<Sale>,
        businessId: Long,
        windowDays: Int = 14
    ): List<ProductAlert> {
        val now = System.currentTimeMillis()
        val windowStart = now - (windowDays * 24L * 60 * 60 * 1000)

        // Group sales by productId
        val salesByProduct = sales.groupBy { it.productId }

        return products.filter { product ->
            val hasStock = product.stockQuantity > 0
            val existsLongEnough = product.createdAt <= windowStart
            val noRecentSales = salesByProduct[product.id]?.none { it.timestamp >= windowStart } ?: true
            hasStock && existsLongEnough && noRecentSales
        }.map { product ->
            ProductAlert(
                businessId = businessId,
                productId = product.id,
                type = "slow_mover",
                message = "${product.name} hasn't sold in $windowDays days — consider a promotion or price review.",
                severity = "info",
                isRead = false,
                createdAt = now
            )
        }
    }

    /**
     * Compute average daily units sold over the given window.
     * Used by both the restock suggestion and the analytics dashboard.
     */
    fun computeSalesVelocity(
        productId: Long,
        sales: List<Sale>,
        windowDays: Int = 7
    ): Double {
        val now = System.currentTimeMillis()
        val windowStart = now - (windowDays * 24L * 60 * 60 * 1000)
        val unitsSold = sales.count { it.productId == productId && it.timestamp >= windowStart }
        return unitsSold.toDouble() / windowDays
    }

    /**
     * Check if a product needs restocking.
     * If stockQuantity / velocity < 7, product is projected to run out in under 7 days.
     */
    fun checkRestockSuggestion(
        product: Product,
        velocity: Double
    ): ProductAlert? {
        if (velocity <= 0.0) return null // Don't suggest restocking something that isn't moving
        val daysRemaining = product.stockQuantity / velocity
        if (daysRemaining < 7) {
            val daysRounded = daysRemaining.toInt().coerceAtLeast(1)
            return ProductAlert(
                businessId = product.businessId,
                productId = product.id,
                type = "restock_suggestion",
                message = "${product.name} is selling fast — at this pace you'll run out in ~$daysRounded days.",
                severity = "warning",
                isRead = false,
                createdAt = System.currentTimeMillis()
            )
        }
        return null
    }

    /**
     * Generate all alerts for a business in one pass.
     */
    fun generateAllAlerts(
        products: List<Product>,
        sales: List<Sale>,
        businessId: Long
    ): List<ProductAlert> {
        val lowStockAlerts = checkLowStock(products, businessId)
        val slowMoverAlerts = checkSlowMovers(products, sales, businessId)
        val restockAlerts = products.mapNotNull { product ->
            val velocity = computeSalesVelocity(product.id, sales)
            checkRestockSuggestion(product, velocity)
        }
        return lowStockAlerts + slowMoverAlerts + restockAlerts
    }
}
