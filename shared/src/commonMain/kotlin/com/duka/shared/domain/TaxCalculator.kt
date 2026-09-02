package com.duka.shared.domain

/**
 * Pure function to compute tax tier from annual turnover.
 * Rwanda small-retail tax tiers (simplified for this phase).
 */
object TaxCalculator {

    enum class TaxTier(val label: String, val description: String) {
        EXEMPT("Exempt", "Annual turnover < 2,000,000 RWF — no income tax"),
        FLAT("Flat Micro Tax", "Annual turnover 2M–12M RWF — fixed annual amount"),
        LUMP_SUM("Lump Sum", "Annual turnover 12M–20M RWF — 3% of turnover"),
        FULL_VAT("Full VAT", "Annual turnover > 20M RWF — 18% VAT + mandatory EBM")
    }

    data class TaxResult(
        val tier: TaxTier,
        val annualTurnover: Double,
        val estimatedTax: Double,
        val vatCollected: Double
    )

    private const val FLAT_MICRO_TAX_ANNUAL = 200_000.0
    private const val VAT_RATE = 0.18

    fun compute(quarterlyTurnover: Double): TaxResult {
        val annualTurnover = quarterlyTurnover * 4

        val tier = when {
            annualTurnover < 2_000_000 -> TaxTier.EXEMPT
            annualTurnover <= 12_000_000 -> TaxTier.FLAT
            annualTurnover <= 20_000_000 -> TaxTier.LUMP_SUM
            else -> TaxTier.FULL_VAT
        }

        val estimatedTax = when (tier) {
            TaxTier.EXEMPT -> 0.0
            TaxTier.FLAT -> FLAT_MICRO_TAX_ANNUAL
            TaxTier.LUMP_SUM -> annualTurnover * 0.03
            TaxTier.FULL_VAT -> annualTurnover * VAT_RATE
        }

        val vatCollected = if (tier == TaxTier.FULL_VAT) {
            quarterlyTurnover * VAT_RATE
        } else 0.0

        return TaxResult(
            tier = tier,
            annualTurnover = annualTurnover,
            estimatedTax = estimatedTax,
            vatCollected = vatCollected
        )
    }
}
