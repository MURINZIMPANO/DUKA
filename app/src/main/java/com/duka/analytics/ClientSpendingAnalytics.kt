package com.duka.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.BudgetGoal
import com.duka.app.data.local.entity.Purchase
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

/**
 * Spending breakdown section for Client Budget (C4).
 * Shows spending by business, month-to-date, 4-week trend, and budget vs actual.
 */
@Composable
fun SpendingBreakdownSection(
    purchases: List<Purchase>,
    businessNames: Map<Long, String>,
    budgetGoal: BudgetGoal?,
    clientUserId: Long
) {
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }
    val zone = ZoneId.systemDefault()
    val now = System.currentTimeMillis()
    val today = LocalDate.now()

    // Current week range
    val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .atStartOfDay(zone).toInstant().toEpochMilli()

    // Current month range
    val monthStart = today.withDayOfMonth(1)
        .atStartOfDay(zone).toInstant().toEpochMilli()

    // This week's purchases
    val thisWeekPurchases = purchases.filter { it.timestamp >= weekStart }
    val thisMonthPurchases = purchases.filter { it.timestamp >= monthStart }

    // Spending by business this week
    val spendingByBusiness = thisWeekPurchases.groupBy { it.businessId }
        .mapValues { (_, p) -> p.sumOf { it.amount.toLong() } }
        .toList()
        .sortedByDescending { it.second }

    // Month-to-date total
    val monthTotal = thisMonthPurchases.sumOf { it.amount.toLong() }
    val weeklyLimit = budgetGoal?.weeklyLimit?.toLong() ?: 25000L
    val monthlyBudget = (weeklyLimit * 4.33).toLong()
    val monthlyRatio = if (monthlyBudget > 0) monthTotal.toDouble() / monthlyBudget else 0.0

    // Last 4 weeks trend
    val weeks = (0..3).map { weekOffset ->
        val wStart = today.minusWeeks((weekOffset + 1).toLong())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(zone).toInstant().toEpochMilli()
        val wEnd = today.minusWeeks(weekOffset.toLong())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(zone).toInstant().toEpochMilli()
        val label = today.minusWeeks(weekOffset.toLong())
            .format(DateTimeFormatter.ofPattern("MMM d"))
        val total = purchases.filter { it.timestamp >= wStart && it.timestamp < wEnd }
            .sumOf { it.amount.toLong() }
        label to total
    }.reversed()

    // Budget vs actual rows
    val thisWeekTotal = thisWeekPurchases.sumOf { it.amount.toLong() }
    val lastWeekStart = today.minusWeeks(1)
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .atStartOfDay(zone).toInstant().toEpochMilli()
    val lastWeekTotal = purchases.filter { it.timestamp >= lastWeekStart && it.timestamp < weekStart }
        .sumOf { it.amount.toLong() }
    val lastMonthStart = today.minusMonths(1).withDayOfMonth(1)
        .atStartOfDay(zone).toInstant().toEpochMilli()
    val lastMonthEnd = monthStart
    val lastMonthTotal = purchases.filter { it.timestamp >= lastMonthStart && it.timestamp < lastMonthEnd }
        .sumOf { it.amount.toLong() }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Section header
        Text(
            "Spending breakdown",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Ink)
        )

        // Spending by business (horizontal bar chart)
        if (spendingByBusiness.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Spending by business (this week)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val maxSpending = spendingByBusiness.maxOfOrNull { it.second } ?: 1L

                    spendingByBusiness.forEach { (bizId, total) ->
                        val name = (businessNames[bizId] ?: "Business #$bizId").take(12)
                        val ratio = total.toFloat() / maxSpending

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                name,
                                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant),
                                modifier = Modifier.weight(0.3f)
                            )
                            // Bar
                            Box(modifier = Modifier.weight(0.5f).height(12.dp)) {
                                Canvas(modifier = Modifier.fillMaxWidth().height(12.dp)) {
                                    drawRoundRect(
                                        color = Forest,
                                        topLeft = Offset.Zero,
                                        size = Size(size.width * ratio, size.height),
                                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                    )
                                    drawRoundRect(
                                        color = Mist,
                                        topLeft = Offset(size.width * ratio, 0f),
                                        size = Size(size.width * (1f - ratio), size.height),
                                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                    )
                                }
                            }
                            Text(
                                "${currencyFormat.format(total)} RWF",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    color = Forest
                                ),
                                modifier = Modifier.weight(0.2f)
                            )
                        }
                    }
                }
            }
        }

        // Month-to-date summary card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Month to date",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${currencyFormat.format(monthTotal)} RWF",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Forest
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                val diff = monthlyBudget - monthTotal
                val chipColor = when {
                    diff <= 0 -> Clay
                    monthlyRatio > 0.8 -> Amber
                    else -> Forest
                }
                val chipText = when {
                    diff <= 0 -> "Over budget by ${currencyFormat.format(kotlin.math.abs(diff))} RWF"
                    else -> "${currencyFormat.format(diff)} RWF remaining"
                }
                Surface(shape = RoundedCornerShape(8.dp), color = chipColor.copy(alpha = 0.1f)) {
                    Text(
                        chipText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = chipColor)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${thisMonthPurchases.size} purchase${if (thisMonthPurchases.size != 1) "s" else ""} across ${thisMonthPurchases.map { it.businessId }.distinct().size} shop${if (thisMonthPurchases.map { it.businessId }.distinct().size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                )
            }
        }

        // Spending trend (last 4 weeks)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Spending trend (last 4 weeks)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val maxWeek = weeks.maxOfOrNull { it.second } ?: 1L

                Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                    val barWidth = (size.width / 4f) * 0.6f
                    val spacing = (size.width / 4f) * 0.4f
                    val chartHeight = size.height - 20.dp.toPx()

                    weeks.forEachIndexed { index, (_, total) ->
                        val barHeight = if (maxWeek > 0) (total.toFloat() / maxWeek) * chartHeight else 0f
                        val x = index * (barWidth + spacing) + spacing / 2
                        val y = chartHeight - barHeight

                        drawRoundRect(
                            color = Forest,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    weeks.forEach { (label, _) ->
                        Text(
                            label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Ink.copy(alpha = 0.6f),
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }

        // Budget vs actual table
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Budget vs actual",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Period", modifier = Modifier.weight(0.25f), style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                    Text("Budget", modifier = Modifier.weight(0.25f), style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                    Text("Spent", modifier = Modifier.weight(0.25f), style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                    Text("Status", modifier = Modifier.weight(0.25f), style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Rows
                data class BudgetRow(val period: String, val budget: Long, val spent: Long)
                val rows = listOf(
                    BudgetRow("This week", weeklyLimit, thisWeekTotal),
                    BudgetRow("This month", monthlyBudget, monthTotal),
                    BudgetRow("Last week", weeklyLimit, lastWeekTotal),
                    BudgetRow("Last month", monthlyBudget, lastMonthTotal)
                )

                rows.forEach { row ->
                    val ratio = if (row.budget > 0) row.spent.toDouble() / row.budget else 0.0
                    val statusText: String
                    val statusColor: Color
                    when {
                        ratio > 1.0 -> { statusText = "Over budget"; statusColor = Clay }
                        ratio > 0.8 -> { statusText = "Close (>80%)"; statusColor = Amber }
                        else -> { statusText = "Under budget"; statusColor = Forest }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(row.period, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.25f))
                        Text(
                            "${currencyFormat.format(row.budget)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            modifier = Modifier.weight(0.25f)
                        )
                        Text(
                            "${currencyFormat.format(row.spent)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, color = Forest),
                            modifier = Modifier.weight(0.25f)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = statusColor.copy(alpha = 0.1f),
                            modifier = Modifier.weight(0.25f)
                        ) {
                            Text(
                                statusText,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = statusColor, fontSize = 9.sp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
