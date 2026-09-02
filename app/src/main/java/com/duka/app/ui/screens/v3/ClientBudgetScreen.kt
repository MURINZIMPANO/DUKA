package com.duka.app.ui.screens.v3

/**
 * Client Budget Screen (C4) — "Your spending"
 *
 * SCOPE-GUARD: READS from V3 Purchase and BudgetGoal tables only.
 * Does NOT touch V1/V2 tables directly.
 *
 * MOCK STATUS: Budget limits are locally-set. No real financial tracking or bank integration.
 */

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.BudgetGoal
import com.duka.app.data.repository.BudgetGoalRepository
import com.duka.app.data.repository.PurchaseRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import com.duka.analytics.SpendingBreakdownSection
import com.duka.analytics.charts.DukaPieChart
import com.duka.analytics.charts.PieSlice
import com.duka.analytics.charts.colorForSlice
import com.duka.app.data.local.dao.BusinessSpend
import com.duka.app.data.local.dao.CategoryRevenue
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import androidx.compose.foundation.layout.size
import com.duka.app.data.repository.BusinessRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientBudgetScreen(
    clientUserId: Long,
    budgetGoalRepository: BudgetGoalRepository,
    purchaseRepository: PurchaseRepository,
    businessRepository: BusinessRepository? = null,
    onBack: (() -> Unit)? = null,
    onNavigateToSettings: (() -> Unit)? = null,
    onLogout: (() -> Unit)? = null
) {
    val budgetGoal by budgetGoalRepository.getForClient(clientUserId).collectAsState(initial = null)
    val purchases by purchaseRepository.getByClient(clientUserId).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    // Calculate this week's spending
    val weekStart = remember {
        LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val now = remember { System.currentTimeMillis() }
    val daysLeftInWeek = remember {
        (7 - LocalDate.now().dayOfWeek.value).coerceAtLeast(1)
    }

    var weeklySpend by remember { mutableDoubleStateOf(0.0) }
    LaunchedEffect(clientUserId) {
        weeklySpend = purchaseRepository.getTotalSpentInRange(clientUserId, weekStart, now) ?: 0.0
    }

    val weeklyLimit = budgetGoal?.weeklyLimit ?: 25000.0
    val spendRatio = if (weeklyLimit > 0) (weeklySpend / weeklyLimit).coerceIn(0.0, 1.0) else 0.0
    val animatedProgress by animateFloatAsState(
        targetValue = spendRatio.toFloat(),
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "progress"
    )
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    val isNearLimit = spendRatio > 0.8
    val isOverLimit = spendRatio >= 1.0

    // Edit budget bottom sheet
    var showEditSheet by remember { mutableStateOf(false) }
    var editBudgetValue by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    var showProfileSheet by remember { mutableStateOf(false) }

    if (showEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Set weekly budget",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                OutlinedTextField(
                    value = editBudgetValue,
                    onValueChange = { editBudgetValue = it },
                    label = { Text("Weekly limit (RWF)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )
                androidx.compose.material3.Button(
                    onClick = {
                        val limit = editBudgetValue.toDoubleOrNull()
                        if (limit != null && limit > 0) {
                            scope.launch {
                                budgetGoalRepository.upsert(
                                    BudgetGoal(
                                        clientUserId = clientUserId,
                                        weeklyLimit = limit
                                    )
                                )
                                showEditSheet = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        // Decorative backdrop
        DecorativeBackdrop(
            page = DukaPage.CLIENT_BUDGET,
            modifier = Modifier.fillMaxSize()
        )

        // C4 scroll fix: Cause B + D — Scaffold must fillMaxSize so LazyColumn gets full height;
        // contentPadding bottom increased to clear bottom navigation bar.
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Client dashboard",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                "Your spending",
                                color = White,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    actions = {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Amber,
                                    modifier = Modifier.size(8.dp)
                                )
                            }
                        ) {
                            Icon(
                                Icons.Outlined.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = White
                            )
                        }
                        IconButton(onClick = { showProfileSheet = true }) {
                            Icon(
                                Icons.Outlined.Settings,
                                contentDescription = "Settings",
                                tint = White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 96.dp // leave room above the bottom navigation bar
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Weekly budget card
                item(key = "weekly_budget") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Header row with edit button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Weekly budget",
                                    style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant)
                                )
                                IconButton(
                                    onClick = {
                                        editBudgetValue = weeklyLimit.toLong().toString()
                                        showEditSheet = true
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.Edit,
                                        contentDescription = "Edit budget",
                                        modifier = Modifier.size(18.dp),
                                        tint = OnSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Spend / limit
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    "${currencyFormat.format(weeklySpend.toLong())} / ${currencyFormat.format(weeklyLimit.toLong())} RWF",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = when {
                                            isOverLimit -> Clay
                                            isNearLimit -> Amber
                                            else -> Forest
                                        }
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Animated progress bar
                            LinearProgressIndicator(
                                progress = animatedProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = when {
                                    isOverLimit -> Clay
                                    isNearLimit -> Amber
                                    else -> Forest
                                },
                                trackColor = Mist,
                            )
                        }
                    }
                }

                // Tip card — shown when spend > 80%
                if (isNearLimit && !isOverLimit) {
                    item(key = "tip_near_limit") {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Amber.copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "You're close to your weekly limit \u2014 $daysLeftInWeek days left in the week.",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(color = Amber)
                            )
                        }
                    }
                }

                if (isOverLimit) {
                    item(key = "over_budget_warning") {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Clay.copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "You've exceeded your weekly budget.",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(color = Clay)
                            )
                        }
                    }
                }

                // Recent purchases
                item(key = "recent_purchases_header") {
                    Text(
                        "Recent purchases",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    )
                }

                if (purchases.isEmpty()) {
                    item(key = "empty_purchases") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Filled.ReceiptLong,
                                    contentDescription = "No purchases",
                                    modifier = Modifier.size(32.dp),
                                    tint = OnSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "No purchases yet.",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                                )
                            }
                        }
                    }
                }

                items(
                    items = purchases.take(10),
                    key = { purchase -> purchase.id }
                ) { purchase ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Product #${purchase.productId}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    java.time.Instant.ofEpochMilli(purchase.timestamp)
                                        .atZone(ZoneId.systemDefault())
                                        .format(java.time.format.DateTimeFormatter.ofPattern("MMM d, HH:mm")),
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                )
                            }
                            Text(
                                "${currencyFormat.format(purchase.amount.toLong())} RWF",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Forest
                                )
                            )
                        }
                    }
                }

                // V6: Spending Breakdown Section
                item(key = "spending_breakdown") {
                    Spacer(modifier = Modifier.height(8.dp))
                    SpendingBreakdownSection(
                        purchases = purchases,
                        businessNames = emptyMap(), // Will be populated with business names
                        budgetGoal = budgetGoal,
                        clientUserId = clientUserId
                    )
                }

                // Pie charts: Spending by category and by shop
                item(key = "pie_charts") {
                    var categorySpending by remember { mutableStateOf<List<CategoryRevenue>>(emptyList()) }
                    var businessSpending by remember { mutableStateOf<List<BusinessSpend>>(emptyList()) }
                    var businessNames by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }

                    // Time filter: this week by default
                    val today = remember { LocalDate.now() }
                    val weekStartMs = remember {
                        today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    }
                    val nowMs = remember { System.currentTimeMillis() }

                    LaunchedEffect(clientUserId) {
                        categorySpending = purchaseRepository.spendingByCategory(clientUserId, weekStartMs, nowMs)
                        businessSpending = purchaseRepository.spendingByBusiness(clientUserId, weekStartMs, nowMs)
                        // Resolve business names
                        if (businessRepository != null) {
                            val names = mutableMapOf<Long, String>()
                            businessSpending.forEach { spend ->
                                val biz = businessRepository.getById(spend.businessId)
                                if (biz != null) names[spend.businessId] = biz.name
                            }
                            businessNames = names
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Where your money goes",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            ),
                            modifier = Modifier.padding(start = 0.dp)
                        )

                        // Spending by category
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "By product category",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                DukaPieChart(
                                    slices = categorySpending.map { entry ->
                                        PieSlice(
                                            label = entry.category,
                                            value = entry.total.toFloat(),
                                            color = colorForSlice(entry.category)
                                        )
                                    },
                                    centerLabel = "Spent",
                                    centerValue = "${currencyFormat.format(categorySpending.sumOf { it.total }.toLong())} RWF",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Spending by shop
                        if (businessSpending.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "By shop",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DukaPieChart(
                                        slices = businessSpending.map { entry ->
                                            PieSlice(
                                                label = businessNames[entry.businessId] ?: "Business #${entry.businessId}",
                                                value = entry.total.toFloat(),
                                                color = colorForSlice("business_${entry.businessId}")
                                            )
                                        },
                                        centerLabel = "Spent",
                                        centerValue = "${currencyFormat.format(businessSpending.sumOf { it.total }.toLong())} RWF",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Profile bottom sheet with Settings and Logout
    if (showProfileSheet) {
        ModalBottomSheet(
            onDismissRequest = { showProfileSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Profile",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showProfileSheet = false
                            onNavigateToSettings?.invoke()
                        }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = Forest,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Settings", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showProfileSheet = false
                            onLogout?.invoke()
                        }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = Clay,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Log out", style = MaterialTheme.typography.bodyMedium.copy(color = Clay))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
