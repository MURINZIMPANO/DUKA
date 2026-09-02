package com.duka.shared.domain

/**
 * V3/V4 Domain — Pure functions for Marketplace + Insight + Supply Network.
 * No platform dependencies.
 */
object CreditScoreCalculator {

    data class CreditBreakdown(
        val score: Int,
        val label: String,
        val factors: List<String>,
        val recommendation: String
    )

    fun compute(
        totalMonths: Int,
        monthsWithSales: Int,
        monthlyIncomes: List<Double>,
        totalSales: Int,
        totalReceipts: Int
    ): CreditBreakdown {
        if (totalMonths == 0) {
            return CreditBreakdown(
                0, "Insufficient Data",
                listOf("Business too new"),
                "Keep selling to build history"
            )
        }

        val consistency = (monthsWithSales.toFloat() / totalMonths).coerceIn(0f, 1f)
        val consistencyScore = (consistency * 35).toInt()

        val avgIncome = if (monthlyIncomes.isNotEmpty()) monthlyIncomes.average() else 0.0
        val variance = if (monthlyIncomes.size > 1) {
            monthlyIncomes.map { (it - avgIncome) * (it - avgIncome) }.average()
        } else 0.0
        val cv = if (avgIncome > 0) kotlin.math.sqrt(variance) / avgIncome else 1.0
        val stabilityScore = ((1.0 - cv.coerceIn(0.0, 1.0)) * 25).toInt()

        val ebmRatio = if (totalSales > 0) (totalReceipts.toFloat() / totalSales).coerceIn(0f, 1f) else 0f
        val ebmScore = (ebmRatio * 25).toInt()

        val maturityScore = ((totalMonths.coerceAtMost(24).toFloat() / 24) * 15).toInt()

        val totalScore = consistencyScore + stabilityScore + ebmScore + maturityScore

        val label = when {
            totalScore >= 80 -> "Excellent"
            totalScore >= 60 -> "Good"
            totalScore >= 40 -> "Fair"
            else -> "Building"
        }

        val factors = mutableListOf<String>()
        factors.add("Sales consistency: ${(consistency * 100).toInt()}% of months active")
        if (monthlyIncomes.isNotEmpty()) {
            factors.add("Income stability: avg RWF ${avgIncome.toInt()}/month")
        }
        factors.add("EBM compliance: ${(ebmRatio * 100).toInt()}% of sales receipted")
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

    fun compute(
        productId: Long,
        productName: String,
        currentStock: Int,
        recentSalesCount: Int,
        lowStockThreshold: Int = 10
    ): ReorderSuggestion? {
        if (currentStock > lowStockThreshold * 3) return null

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

object MockDistanceCalculator {
    fun computeDistance(businessName: String): Double {
        val hash = businessName.hashCode().toLong()
        val normalized = ((hash and 0x7FFFFFFFL) % 1480).toDouble() / 100.0 + 0.2
        return normalized
    }
}
