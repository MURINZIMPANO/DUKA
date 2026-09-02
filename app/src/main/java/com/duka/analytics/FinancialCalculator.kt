package com.duka.analytics

import com.duka.app.data.local.entity.Product
import com.duka.app.data.local.entity.Sale
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Pure calculation functions for owner analytics — no Android dependencies.
 * All monetary values are in RWF (Long for integer amounts, Double for averages).
 */
object FinancialCalculator {

    data class TaxCalculation(
        val tier: String,           // "EXEMPT" | "FLAT_MICRO" | "LUMP_SUM" | "FULL_VAT"
        val annualisedRevenue: Long,
        val taxOwed: Long,          // 0 for EXEMPT; band amount for FLAT_MICRO; 3% for LUMP_SUM; 18% for FULL_VAT
        val vatCollected: Long,     // 0 unless FULL_VAT tier
        val quarterlyEstimate: Long // taxOwed / 4, for planning
    )

    data class ProfitBreakdown(
        val grossProfit: Long,
        val netProfit: Long,
        val totalRevenue: Long,
        val totalCost: Long,
        val totalExpenses: Long,
        val hasCompleteCostData: Boolean
    )

    // Fixed band for flat micro tax (RWF per year) — same as TaxCalculator
    private const val FLAT_MICRO_TAX_ANNUAL = 200_000L
    private const val VAT_RATE = 0.18

    fun totalRevenue(sales: List<Sale>): Long {
        return sales.sumOf { it.amount.toLong() }
    }

    fun totalTransactions(sales: List<Sale>): Int {
        return sales.size
    }

    fun averageOrderValue(sales: List<Sale>): Double {
        if (sales.isEmpty()) return 0.0
        return totalRevenue(sales).toDouble() / sales.size
    }

    fun grossProfit(sales: List<Sale>, products: Map<String, Product>): Long {
        var profit = 0L
        var hasCostData = true
        for (sale in sales) {
            val product = products[sale.productId.toString()]
            if (product != null && product.costPrice > 0) {
                profit += sale.amount.toLong() - product.costPrice
            } else if (product != null) {
                hasCostData = false
                profit += sale.amount.toLong() // No cost data, treat as pure revenue
            }
        }
        return profit
    }

    fun netProfit(gross: Long, expenses: Long): Long {
        return gross - expenses
    }

    fun taxOwed(annualisedRevenue: Long): TaxCalculation {
        val tier = when {
            annualisedRevenue < 2_000_000 -> "EXEMPT"
            annualisedRevenue <= 12_000_000 -> "FLAT_MICRO"
            annualisedRevenue <= 20_000_000 -> "LUMP_SUM"
            else -> "FULL_VAT"
        }

        val taxOwed = when (tier) {
            "EXEMPT" -> 0L
            "FLAT_MICRO" -> FLAT_MICRO_TAX_ANNUAL
            "LUMP_SUM" -> (annualisedRevenue * 0.03).toLong()
            "FULL_VAT" -> (annualisedRevenue * VAT_RATE).toLong()
            else -> 0L
        }

        val vatCollected = if (tier == "FULL_VAT") {
            (annualisedRevenue * VAT_RATE).toLong()
        } else 0L

        return TaxCalculation(
            tier = tier,
            annualisedRevenue = annualisedRevenue,
            taxOwed = taxOwed,
            vatCollected = vatCollected,
            quarterlyEstimate = taxOwed / 4
        )
    }

    fun topSellingProducts(sales: List<Sale>, n: Int = 5): List<Pair<Long, Int>> {
        return sales.groupBy { it.productId }
            .map { (productId, salesList) -> productId to salesList.size }
            .sortedByDescending { it.second }
            .take(n)
    }

    fun revenueByDay(sales: List<Sale>): Map<LocalDate, Long> {
        val zone = ZoneId.systemDefault()
        return sales.groupBy { sale ->
            Instant.ofEpochMilli(sale.timestamp).atZone(zone).toLocalDate()
        }.mapValues { (_, daySales) ->
            daySales.sumOf { it.amount.toLong() }
        }
    }

    fun revenueByMonth(sales: List<Sale>): Map<LocalDate, Long> {
        val zone = ZoneId.systemDefault()
        return sales.groupBy { sale ->
            Instant.ofEpochMilli(sale.timestamp).atZone(zone).toLocalDate().withDayOfMonth(1)
        }.mapValues { (_, monthSales) ->
            monthSales.sumOf { it.amount.toLong() }
        }
    }

    /**
     * Compute profit breakdown for a given window.
     */
    fun computeProfitBreakdown(
        sales: List<Sale>,
        products: Map<Long, Product>,
        expenses: Long
    ): ProfitBreakdown {
        val revenue = totalRevenue(sales)
        var totalCost = 0L
        var hasCompleteCostData = true

        for (sale in sales) {
            val product = products[sale.productId]
            if (product != null && product.costPrice > 0) {
                totalCost += product.costPrice
            } else {
                hasCompleteCostData = false
            }
        }

        val gross = revenue - totalCost
        val net = gross - expenses

        return ProfitBreakdown(
            grossProfit = gross,
            netProfit = net,
            totalRevenue = revenue,
            totalCost = totalCost,
            totalExpenses = expenses,
            hasCompleteCostData = hasCompleteCostData
        )
    }
}
