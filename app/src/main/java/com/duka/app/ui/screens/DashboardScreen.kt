package com.duka.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.EmployeeRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.components.ShimmerBox
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.ForestLight
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

@Composable
fun DashboardScreen(
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    saleRepository: SaleRepository,
    employeeRepository: EmployeeRepository? = null,
    productAlertRepository: com.duka.app.data.repository.ProductAlertRepository? = null,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToTax: () -> Unit,
    onNavigateToVoiceAdd: () -> Unit,
    onNavigateToTeam: () -> Unit = {},
    onNavigateToAlerts: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)

    // Time ranges
    val todayStart = remember {
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val todayEnd = remember {
        LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val yesterdayStart = remember {
        LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val monthStart = remember {
        LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    val businessId = business?.id ?: 0L

    val todayIncome by saleRepository.getTotalInRange(businessId, todayStart, todayEnd)
        .collectAsState(initial = null)
    val yesterdayIncome by saleRepository.getTotalInRange(businessId, yesterdayStart, todayStart)
        .collectAsState(initial = null)
    val monthIncome by saleRepository.getTotalInRange(businessId, monthStart, todayEnd)
        .collectAsState(initial = null)
    val todaySalesCount by saleRepository.getSalesCountInRange(businessId, todayStart, todayEnd)
        .collectAsState(initial = 0)
    val topProducts by saleRepository.getTopProducts(businessId, todayStart, todayEnd)
        .collectAsState(initial = emptyList())

    // Team summary
    val activeEmployeeCount by employeeRepository?.getActiveEmployeeCount(businessId)
        ?.collectAsState(initial = 0) ?: remember { mutableStateOf(0) }

    // V6: Unread alert count for badge
    val unreadAlertCount by productAlertRepository?.getUnreadCount(businessId)
        ?.collectAsState(initial = 0) ?: remember { mutableStateOf(0) }

    // Look up product names for top products
    var productNames by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }
    LaunchedEffect(topProducts) {
        if (topProducts.isNotEmpty()) {
            val ids = topProducts.map { it.productId }
            val products = productRepository.getProductsByIds(ids)
            productNames = products.associate { it.id to it.name }
        }
    }

    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    val todayTotal = todayIncome ?: 0.0
    val yesterdayTotal = yesterdayIncome ?: 0.0
    val vsYesterday = if (yesterdayTotal > 0) {
        ((todayTotal - yesterdayTotal) / yesterdayTotal * 100)
    } else if (todayTotal > 0) 100.0 else 0.0

    // Animated income value for count-up effect
    var animatedIncome by remember { mutableStateOf(0.0) }
    LaunchedEffect(todayTotal) {
        // Simple count-up animation
        val steps = 20
        val stepValue = todayTotal / steps
        for (i in 1..steps) {
            animatedIncome = stepValue * i
            kotlinx.coroutines.delay(30)
        }
        animatedIncome = todayTotal
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.DASHBOARD,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = business?.name ?: "Duka",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        )
                    }
                },
                actions = {
                    // Offline-ready chip (proper icon, not emoji)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ForestLight,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.OfflineBolt,
                                contentDescription = stringResource(R.string.dashboard_offline_ready),
                                modifier = Modifier.size(12.dp),
                                tint = Amber
                            )
                            Text(
                                stringResource(R.string.dashboard_offline_ready),
                                style = MaterialTheme.typography.labelSmall.copy(color = White)
                            )
                        }
                    }
                    // Settings icon
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = White
                        )
                    }
                    // Notification bell with unread count badge
                    IconButton(onClick = onNavigateToAlerts) {
                        Box {
                            Icon(
                                Icons.Filled.NotificationsNone,
                                contentDescription = stringResource(R.string.dashboard_notifications),
                                tint = White
                            )
                            if (unreadAlertCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = 2.dp, y = (-2).dp)
                                        .clip(CircleShape)
                                        .background(Amber)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Forest
                )
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Voice add FAB (proper Material icon, not emoji)
                SmallFloatingActionButton(
                    onClick = onNavigateToVoiceAdd,
                    containerColor = Amber,
                    contentColor = Ink
                ) {
                    Icon(Icons.Filled.Mic, contentDescription = "Voice add product")
                }
                // Add product FAB
                FloatingActionButton(
                    onClick = onNavigateToAddProduct,
                    containerColor = Forest,
                    contentColor = White
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Product")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Today's income highlight card with count-up animation
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Forest)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            stringResource(R.string.dashboard_today_income),
                            style = MaterialTheme.typography.labelMedium.copy(color = White.copy(alpha = 0.7f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Animated count-up for income
                        AnimatedContent(
                            targetState = currencyFormat.format(animatedIncome),
                            transitionSpec = {
                                fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                            },
                            label = "incomeAnimation"
                        ) { target ->
                            Text(
                                "RWF $target",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = White,
                                    fontSize = 28.sp
                                )
                            )
                        }
                        if (todayTotal > 0 || yesterdayTotal > 0) {
                            val sign = if (vsYesterday >= 0) "+" else ""
                            Text(
                                "$sign${String.format("%.0f", vsYesterday)}% vs yesterday",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (vsYesterday >= 0) Amber else Clay
                                )
                            )
                        }
                    }
                }
            }

            // Stats row with staggered card entry
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // This month card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                stringResource(R.string.dashboard_this_month),
                                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "RWF ${currencyFormat.format(monthIncome ?: 0.0)}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Forest
                                )
                            )
                        }
                    }

                    // Products sold today card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                stringResource(R.string.dashboard_products_sold),
                                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "$todaySalesCount",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Forest
                                )
                            )
                        }
                    }
                }
            }

            // Team summary card
            if (employeeRepository != null && activeEmployeeCount > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        onClick = onNavigateToTeam
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Group,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Forest
                            )
                            Text(
                                "$activeEmployeeCount employee${if (activeEmployeeCount != 1) "s" else ""} active",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "Manage →",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                            )
                        }
                    }
                }
            }

            // Top products section with staggered entry
            item {
                Text(
                    stringResource(R.string.dashboard_top_products),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (topProducts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Shimmer loading placeholder for empty state
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                stringResource(R.string.dashboard_no_sales),
                                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                            )
                        }
                    }
                }
            }

            // Staggered card entry animation for top products
            itemsIndexed(topProducts) { index, item ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay((index * 40).toLong())
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(300)) + slideInVertically(tween(300, easing = FastOutSlowInEasing))
                ) {
                    val productName = productNames[item.productId] ?: "Product #${item.productId}"
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
                            Text(
                                text = productName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Forest.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.label_sold_count, item.cnt),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(color = Forest)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    }
}
