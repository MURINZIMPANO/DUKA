package com.duka.analytics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.dao.CategoryRevenue
import com.duka.app.data.local.dao.SourceRevenue
import com.duka.app.data.local.entity.Expense
import com.duka.app.data.local.entity.Product
import com.duka.app.data.local.entity.Sale
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ExpenseRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.analytics.charts.DukaPieChart
import com.duka.analytics.charts.PieSlice
import com.duka.analytics.charts.colorForSlice
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.ForestLight
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.text.NumberFormat
import java.util.Locale

private enum class AnalyticsPeriod(val label: String) {
    DAY("Day"), WEEK("Week"), MONTH("Month"), YEAR("Year")
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun AnalyticsHubScreen(
    businessRepository: BusinessRepository,
    saleRepository: SaleRepository,
    productRepository: ProductRepository,
    expenseRepository: ExpenseRepository,
    onBack: () -> Unit,
    onNavigateToTaxEbm: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val businessId = business?.id ?: 0L
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { AnalyticsPeriod.entries.size })

    // Data state
    var currentSales by remember { mutableStateOf<List<Sale>>(emptyList()) }
    var previousSales by remember { mutableStateOf<List<Sale>>(emptyList()) }
    var allProducts by remember { mutableStateOf<Map<Long, Product>>(emptyMap()) }
    var currentExpenses by remember { mutableLongStateOf(0L) }

    // Pie chart data
    var categoryRevenue by remember { mutableStateOf<List<CategoryRevenue>>(emptyList()) }
    var sourceRevenue by remember { mutableStateOf<List<SourceRevenue>>(emptyList()) }

    // Expense sheet
    var showExpenseSheet by remember { mutableStateOf(false) }
    var expenseLabel by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }

    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }
    val zone = ZoneId.systemDefault()

    // Calculate time ranges for each period
    fun getTimeRange(period: AnalyticsPeriod): Pair<Long, Long> {
        val now = System.currentTimeMillis()
        val today = LocalDate.now()
        return when (period) {
            AnalyticsPeriod.DAY -> {
                val start = today.atStartOfDay(zone).toInstant().toEpochMilli()
                val end = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
                start to end
            }
            AnalyticsPeriod.WEEK -> {
                val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val start = weekStart.atStartOfDay(zone).toInstant().toEpochMilli()
                now to start
            }
            AnalyticsPeriod.MONTH -> {
                val monthStart = today.withDayOfMonth(1)
                val start = monthStart.atStartOfDay(zone).toInstant().toEpochMilli()
                now to start
            }
            AnalyticsPeriod.YEAR -> {
                val yearStart = today.withDayOfYear(1)
                val start = yearStart.atStartOfDay(zone).toInstant().toEpochMilli()
                now to start
            }
        }
    }

    // Load data for current period
    LaunchedEffect(pagerState.currentPage, businessId) {
        if (businessId <= 0) return@LaunchedEffect
        val period = AnalyticsPeriod.entries[pagerState.currentPage]
        val (periodStart, periodEnd) = getTimeRange(period)

        currentSales = saleRepository.getSalesInRangeOnce(businessId, periodStart, periodEnd)
        currentExpenses = expenseRepository.getTotalInRange(businessId, periodStart, periodEnd)

        // Previous period for comparison
        val windowSize = periodEnd - periodStart
        val prevStart = periodStart - windowSize
        val prevEnd = periodStart
        previousSales = saleRepository.getSalesInRangeOnce(businessId, prevStart, prevEnd)

        // Load products for cost calculation
        val products = productRepository.getAllActiveByBusiness(businessId)
        allProducts = products.associateBy { it.id }

        // Load pie chart data
        categoryRevenue = saleRepository.revenueByCategory(businessId, periodStart, periodEnd)
        sourceRevenue = saleRepository.revenueBySource(businessId, periodStart, periodEnd)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Period tabs
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = White,
                contentColor = Forest,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Forest
                    )
                }
            ) {
                AnalyticsPeriod.entries.forEachIndexed { index, period ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = {
                            Text(
                                period.label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    )
                }
            }

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val period = AnalyticsPeriod.entries[page]
                val periodSales = currentSales
                val prevPeriodSales = previousSales

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Revenue Summary
                    item {
                        RevenueSummaryCard(
                            sales = periodSales,
                            previousSales = prevPeriodSales,
                            period = period,
                            currencyFormat = currencyFormat
                        )
                    }

                    // Card 2: Revenue Bar Chart
                    item {
                        RevenueBarChartCard(
                            sales = periodSales,
                            period = period,
                            currencyFormat = currencyFormat
                        )
                    }

                    // Card 3: Profit Breakdown
                    item {
                        val breakdown = FinancialCalculator.computeProfitBreakdown(
                            periodSales, allProducts.mapKeys { it.key }, currentExpenses
                        )
                        ProfitBreakdownCard(
                            breakdown = breakdown,
                            currencyFormat = currencyFormat,
                            onAddExpense = { showExpenseSheet = true }
                        )
                    }

                    // Card 4: Tax Estimate
                    item {
                        val periodRevenue = FinancialCalculator.totalRevenue(periodSales)
                        val annualisedRevenue = when (period) {
                            AnalyticsPeriod.DAY -> periodRevenue * 365
                            AnalyticsPeriod.WEEK -> periodRevenue * 52
                            AnalyticsPeriod.MONTH -> periodRevenue * 12
                            AnalyticsPeriod.YEAR -> periodRevenue
                        }
                        val taxCalc = FinancialCalculator.taxOwed(annualisedRevenue)
                        TaxEstimateCard(
                            taxCalc = taxCalc,
                            currencyFormat = currencyFormat,
                            onViewTaxBreakdown = onNavigateToTaxEbm
                        )
                    }

                    // Card 5: Top Products
                    item {
                        val topProducts = FinancialCalculator.topSellingProducts(periodSales, 5)
                        TopProductsCard(
                            topProducts = topProducts,
                            allProducts = allProducts,
                            currencyFormat = currencyFormat,
                            onViewAll = onNavigateToProducts
                        )
                    }

                    // Card 6: Revenue by category (pie chart)
                    item {
                        RevenueCategoryPieCard(
                            categoryRevenue = categoryRevenue,
                            currencyFormat = currencyFormat
                        )
                    }

                    // Card 7: Revenue by source (pie chart)
                    item {
                        RevenueSourcePieCard(
                            sourceRevenue = sourceRevenue,
                            currencyFormat = currencyFormat
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }

    // Expense bottom sheet
    if (showExpenseSheet) {
        ModalBottomSheet(
            onDismissRequest = { showExpenseSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Add expense",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                OutlinedTextField(
                    value = expenseLabel,
                    onValueChange = { expenseLabel = it },
                    label = { Text("Label (e.g. Rent, Staff wages)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest, unfocusedBorderColor = Mist,
                        focusedContainerColor = White, unfocusedContainerColor = White
                    )
                )
                OutlinedTextField(
                    value = expenseAmount,
                    onValueChange = { expenseAmount = it.filter { c -> c.isDigit() } },
                    label = { Text("Amount (RWF)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest, unfocusedBorderColor = Mist,
                        focusedContainerColor = White, unfocusedContainerColor = White
                    )
                )
                androidx.compose.material3.Button(
                    onClick = {
                        val amount = expenseAmount.toLongOrNull() ?: return@Button
                        if (expenseLabel.isNotBlank()) {
                            scope.launch {
                                val now = System.currentTimeMillis()
                                expenseRepository.create(
                                    Expense(
                                        businessId = businessId,
                                        label = expenseLabel.trim(),
                                        amount = amount,
                                        periodStart = now - 30L * 24 * 60 * 60 * 1000,
                                        periodEnd = now
                                    )
                                )
                                expenseLabel = ""
                                expenseAmount = ""
                                showExpenseSheet = false
                                // Refresh data
                                delay(100)
                                val period = AnalyticsPeriod.entries[pagerState.currentPage]
                                val (start, end) = getTimeRange(period)
                                currentExpenses = expenseRepository.getTotalInRange(businessId, start, end)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save expense", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
private fun RevenueSummaryCard(
    sales: List<Sale>,
    previousSales: List<Sale>,
    period: AnalyticsPeriod,
    currencyFormat: NumberFormat
) {
    val revenue = FinancialCalculator.totalRevenue(sales)
    val prevRevenue = FinancialCalculator.totalRevenue(previousSales)
    val transactions = FinancialCalculator.totalTransactions(sales)
    val avgOrder = FinancialCalculator.averageOrderValue(sales)
    val changePercent = if (prevRevenue > 0) ((revenue - prevRevenue).toDouble() / prevRevenue * 100) else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Forest)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Total revenue",
                style = MaterialTheme.typography.labelMedium.copy(color = White.copy(alpha = 0.7f))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${currencyFormat.format(revenue)} RWF",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = White
                )
            )
            if (prevRevenue > 0 || revenue > 0) {
                val sign = if (changePercent >= 0) "+" else ""
                val color = if (changePercent >= 0) Amber else Clay
                val prevLabel = when (period) {
                    AnalyticsPeriod.DAY -> "yesterday"
                    AnalyticsPeriod.WEEK -> "last week"
                    AnalyticsPeriod.MONTH -> "last month"
                    AnalyticsPeriod.YEAR -> "last year"
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (changePercent >= 0) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "$sign${String.format("%.0f", changePercent)}% vs $prevLabel",
                        style = MaterialTheme.typography.bodySmall.copy(color = color)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "$transactions transactions · avg ${currencyFormat.format(avgOrder.toLong())} RWF per sale",
                style = MaterialTheme.typography.bodySmall.copy(color = White.copy(alpha = 0.7f))
            )
        }
    }
}

@Composable
private fun RevenueBarChartCard(
    sales: List<Sale>,
    period: AnalyticsPeriod,
    currencyFormat: NumberFormat
) {
    val revenueByDay = remember(sales, period) { FinancialCalculator.revenueByDay(sales) }
    var selectedIndex by remember { mutableStateOf(-1) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Revenue over time",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (revenueByDay.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                        drawLine(
                            color = Mist,
                            start = Offset(0f, size.height - 2.dp.toPx()),
                            end = Offset(size.width, size.height - 2.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                    Text(
                        "No sales in this period",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant),
                        modifier = Modifier.align(Alignment.Center).padding(bottom = 32.dp)
                    )
                }
            } else {
                val sortedDays = revenueByDay.keys.sorted()
                val maxRevenue = revenueByDay.values.maxOrNull() ?: 1L

                // Bar chart
                val barColor = Forest
                val selectedBarColor = Amber

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    val barWidth = (size.width / sortedDays.size.coerceAtLeast(1)) * 0.6f
                    val spacing = (size.width / sortedDays.size.coerceAtLeast(1)) * 0.4f
                    val chartHeight = size.height - 20.dp.toPx()

                    sortedDays.forEachIndexed { index, date ->
                        val revenue = revenueByDay[date] ?: 0L
                        val barHeight = (revenue.toFloat() / maxRevenue) * chartHeight
                        val x = index * (barWidth + spacing) + spacing / 2
                        val y = chartHeight - barHeight

                        drawRoundRect(
                            color = if (index == selectedIndex) selectedBarColor else barColor,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }

                // X-axis labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    sortedDays.takeLast(7).forEach { date ->
                        Text(
                            date.format(DateTimeFormatter.ofPattern("EEE")),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Ink.copy(alpha = 0.6f),
                                fontSize = 9.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Tooltip for selected bar
                if (selectedIndex >= 0 && selectedIndex < sortedDays.size) {
                    val date = sortedDays[selectedIndex]
                    val revenue = revenueByDay[date] ?: 0L
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Mist,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Text(
                            "${date.format(DateTimeFormatter.ofPattern("MMM d"))}: ${currencyFormat.format(revenue)} RWF",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = Forest
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfitBreakdownCard(
    breakdown: FinancialCalculator.ProfitBreakdown,
    currencyFormat: NumberFormat,
    onAddExpense: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Profit breakdown",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onAddExpense, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Add expense", modifier = Modifier.size(18.dp), tint = Forest)
                }
            }

            if (!breakdown.hasCompleteCostData) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Amber.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Text(
                        "Partial cost data — add cost prices for accurate profit",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(color = Amber)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Revenue", style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                    Text(
                        "${currencyFormat.format(breakdown.totalRevenue)} RWF",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Forest)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Cost of goods", style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                    Text(
                        "${currencyFormat.format(breakdown.totalCost)} RWF",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Ink)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Expenses", style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                    Text(
                        "${currencyFormat.format(breakdown.totalExpenses)} RWF",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Ink)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        if (breakdown.netProfit >= 0) "Net profit" else "Net loss",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                    )
                    val profitColor = when {
                        breakdown.netProfit > 0 -> Forest
                        breakdown.netProfit < 0 -> Clay
                        else -> OnSurfaceVariant
                    }
                    Text(
                        "${currencyFormat.format(kotlin.math.abs(breakdown.netProfit))} RWF",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = profitColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val chipText = when {
                breakdown.netProfit > 0 -> "Profit"
                breakdown.netProfit < 0 -> "Loss"
                else -> "Break-even"
            }
            val chipColor = when {
                breakdown.netProfit > 0 -> Forest
                breakdown.netProfit < 0 -> Clay
                else -> Amber
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = chipColor.copy(alpha = 0.1f)
            ) {
                Text(
                    chipText,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium.copy(color = chipColor, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun TaxEstimateCard(
    taxCalc: FinancialCalculator.TaxCalculation,
    currencyFormat: NumberFormat,
    onViewTaxBreakdown: () -> Unit
) {
    val tierColor = when (taxCalc.tier) {
        "EXEMPT" -> Forest
        "FLAT_MICRO" -> Forest
        "LUMP_SUM" -> Amber
        "FULL_VAT" -> Clay
        else -> OnSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Tax estimate",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Annualised revenue: ${currencyFormat.format(taxCalc.annualisedRevenue)} RWF",
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = tierColor.copy(alpha = 0.1f)
            ) {
                Text(
                    taxCalc.tier.replace("_", " "),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall.copy(color = tierColor, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Estimated tax this quarter",
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )
            Text(
                "${currencyFormat.format(taxCalc.quarterlyEstimate)} RWF",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Forest
                )
            )

            if (taxCalc.vatCollected > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "VAT collected: ${currencyFormat.format(taxCalc.vatCollected)} RWF",
                    style = MaterialTheme.typography.bodySmall.copy(color = Clay)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onViewTaxBreakdown) {
                Text("View full tax breakdown →", color = Forest, style = MaterialTheme.typography.bodySmall)
            }

            Text(
                "This estimate is based on your recorded sales — keep your product prices and sales up to date for accuracy.",
                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
            )
        }
    }
}

@Composable
private fun TopProductsCard(
    topProducts: List<Pair<Long, Int>>,
    allProducts: Map<Long, Product>,
    currencyFormat: NumberFormat,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Top products",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (topProducts.isEmpty()) {
                Text(
                    "No sales data yet",
                    style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                )
            } else {
                topProducts.forEachIndexed { index, (productId, units) ->
                    val product = allProducts[productId]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${index + 1}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Amber
                                )
                            )
                            Column {
                                Text(
                                    product?.name ?: "Product #$productId",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    "$units sold",
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                )
                            }
                        }
                        Text(
                            "${currencyFormat.format((product?.price ?: 0.0) * units)} RWF",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = Forest
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            TextButton(onClick = onViewAll) {
                Text("View all products →", color = Forest, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun RevenueCategoryPieCard(
    categoryRevenue: List<CategoryRevenue>,
    currencyFormat: NumberFormat
) {
    val totalRevenue = categoryRevenue.sumOf { it.total }.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Revenue by category",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            DukaPieChart(
                slices = categoryRevenue.map { entry ->
                    PieSlice(
                        label = entry.category,
                        value = entry.total.toFloat(),
                        color = colorForSlice(entry.category)
                    )
                },
                centerLabel = "Total",
                centerValue = "${currencyFormat.format(totalRevenue.toLong())} RWF",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RevenueSourcePieCard(
    sourceRevenue: List<SourceRevenue>,
    currencyFormat: NumberFormat
) {
    val sourceColors = mapOf(
        "owner" to Forest,
        "employee" to ForestLight,
        "voice" to Amber,
        "client" to Clay
    )
    val totalRevenue = sourceRevenue.sumOf { it.total }.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Revenue by source",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Smaller chart (80% of normal size)
            DukaPieChart(
                slices = sourceRevenue.map { entry ->
                    PieSlice(
                        label = entry.source.replaceFirstChar { it.uppercase() },
                        value = entry.total.toFloat(),
                        color = sourceColors[entry.source.lowercase()] ?: colorForSlice(entry.source)
                    )
                },
                centerLabel = "Total",
                centerValue = "${currencyFormat.format(totalRevenue.toLong())} RWF",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
