package com.duka.app.domain

/**
 * V3/V4 Domain — Pure functions for Marketplace + Insight + Supply Network.
 *
 * SCOPE-GUARD: These are pure functions with no side effects. They take data from V1/V2
 * repositories (Sale, Product history) and compute derived values. They do NOT write to
 * any database tables.
 *
 * MOCK STATUS: Credit scores and reorder quantities are computed from local seed data.
 * No real lending partners, credit bureaus, or supply chain APIs are involved.
 */

object CreditScoreCalculator {

    data class CreditBreakdown(
        val score: Int,             // 0-100
        val label: String,          // e.g. "Good", "Excellent"
        val factors: List<String>,  // Human-readable breakdown of what drives the score
        val recommendation: String  // What could improve the score
    )

    /**
     * Compute a credit-readiness score from sales history and EBM compliance.
     *
     * Factors (weighted):
     * - Sales consistency (months with sales / total months) — 35%
     * - Income stability (coefficient of variation of monthly income) — 25%
     * - EBM compliance ratio (receipts sent / sales made) — 25%
     * - Business maturity (months since creation) — 15%
     */
    fun compute(
        totalMonths: Int,             // Months since business creation
        monthsWithSales: Int,         // Months that had at least one sale
        monthlyIncomes: List<Double>, // Monthly income amounts for variance computation
        totalSales: Int,              // Total sales count
        totalReceipts: Int            // Total EBM receipts sent
    ): CreditBreakdown {
        if (totalMonths == 0) {
            return CreditBreakdown(0, "Insufficient Data", listOf("Business too new"), "Keep selling to build history")
        }

        // Factor 1: Sales consistency (35%)
        val consistency = (monthsWithSales.toFloat() / totalMonths).coerceIn(0f, 1f)
        val consistencyScore = (consistency * 35).toInt()

        // Factor 2: Income stability (25%) — lower variance = better
        val avgIncome = if (monthlyIncomes.isNotEmpty()) monthlyIncomes.average() else 0.0
        val variance = if (monthlyIncomes.size > 1) {
            monthlyIncomes.map { (it - avgIncome) * (it - avgIncome) }.average()
        } else 0.0
        val cv = if (avgIncome > 0) Math.sqrt(variance) / avgIncome else 1.0 // Coefficient of variation
        val stabilityScore = ((1.0 - cv.coerceIn(0.0, 1.0)) * 25).toInt()

        // Factor 3: EBM compliance (25%)
        val ebmRatio = if (totalSales > 0) (totalReceipts.toFloat() / totalSales).coerceIn(0f, 1f) else 0f
        val ebmScore = (ebmRatio * 25).toInt()

        // Factor 4: Business maturity (15%)
        val maturityScore = ((totalMonths.coerceAtMost(24).toFloat() / 24) * 15).toInt()

        val totalScore = consistencyScore + stabilityScore + ebmScore + maturityScore

        val label = when {
            totalScore >= 80 -> "Excellent"
            totalScore >= 60 -> "Good"
            totalScore >= 40 -> "Fair"
            else -> "Building"
        }

        val factors = mutableListOf<String>()
        factors.add("Sales consistency: ${String.format("%.0f", consistency * 100)}% of months active")
        if (monthlyIncomes.isNotEmpty()) {
            factors.add("Income stability: avg RWF ${String.format("%.0f", avgIncome)}/month")
        }
        factors.add("EBM compliance: ${String.format("%.0f", ebmRatio * 100)}% of sales receipted")
        factors.add("Business age: $totalMonths months")

        val recommendation = when {
            consistency < 0.5f -> "Aim for consistent monthly sales to improve score"
            ebmRatio < 0.8f -> "Send EBM receipts for all sales to boost compliance"
            cv > 0.5 -> "Work on stabilizing month-to-month income"
            else -> "Maintain current performance — score is strong"
        }

        return CreditBreakdown(
            score = totalScore.coerceIn(0, 100),
            label = label,
            factors = factors,
            recommendation = recommendation
        )
    }
}

object ReorderQuantityCalculator {

    data class ReorderSuggestion(
        val productId: Long,
        val productName: String,
        val currentStock: Int,
        val suggestedQuantity: Int,
        val reason: String
    )

    /**
     * Compute suggested reorder quantity based on recent sales velocity.
     *
     * Rule: Suggest enough stock for 10-14 days of average daily sales.
     * If current stock is below the low-stock threshold, flag for reorder.
     */
    fun compute(
        productId: Long,
        productName: String,
        currentStock: Int,
        recentSalesCount: Int,  // Sales in the last 14 days
        lowStockThreshold: Int = 10
    ): ReorderSuggestion? {
        if (currentStock > lowStockThreshold * 3) return null // Not low

        val avgDailySales = if (recentSalesCount > 0) recentSalesCount.toDouble() / 14.0 else 0.5
        val targetDays = 14
        val targetStock = (avgDailySales * targetDays).toInt().coerceAtLeast(lowStockThreshold)
        val suggestedQty = (targetStock - currentStock).coerceAtLeast(1)

        return ReorderSuggestion(
            productId = productId,
            productName = productName,
            currentStock = currentStock,
            suggestedQuantity = suggestedQty,
            reason = when {
                currentStock == 0 -> "Out of stock — urgent reorder"
                currentStock <= lowStockThreshold / 2 -> "Critically low stock ($currentStock remaining)"
                else -> "Low stock ($currentStock remaining) — avg ${"%.1f".format(avgDailySales)} sold/day"
            }
        )
    }
}

/**
 * Mock distance calculation for Client Discover.
 * In a real app, this would use device GPS + business coordinates.
 * For now, returns a deterministic distance based on business name hash.
 */
object MockDistanceCalculator {
    fun computeDistance(businessName: String): Double {
        // Deterministic pseudo-random distance between 0.2 and 15.0 km
        val hash = businessName.hashCode().toLong()
        val normalized = ((hash and 0x7FFFFFFFL) % 1480).toDouble() / 100.0 + 0.2
        return normalized
    }
}
