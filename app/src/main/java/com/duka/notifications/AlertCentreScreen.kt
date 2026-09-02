package com.duka.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.local.entity.ProductAlert
import com.duka.app.data.repository.ProductAlertRepository
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertCentreScreen(
    businessId: Long,
    productAlertRepository: ProductAlertRepository,
    onBack: () -> Unit,
    onAlertTap: (Long) -> Unit // productId to navigate to Edit Product
) {
    val alerts by productAlertRepository.getByBusiness(businessId).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alerts", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        scope.launch {
                            productAlertRepository.markAllAsRead(businessId)
                        }
                    }) {
                        Text("Mark all read", color = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        if (alerts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.CheckCircleOutline,
                        contentDescription = "No alerts",
                        modifier = Modifier.size(48.dp),
                        tint = Forest
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "All clear — no alerts right now",
                        style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(alerts) { index, alert ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay((index * 30).toLong())
                        visible = true
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(300)) + slideInVertically(tween(300, easing = FastOutSlowInEasing))
                    ) {
                        AlertCard(
                            alert = alert,
                            onTap = {
                                scope.launch {
                                    productAlertRepository.markAsRead(alert.id)
                                }
                                onAlertTap(alert.productId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertCard(
    alert: ProductAlert,
    onTap: () -> Unit
) {
    val severityColor = when (alert.severity) {
        "critical" -> Clay
        "warning" -> Amber
        else -> Forest
    }
    val icon = when (alert.type) {
        "low_stock" -> Icons.Outlined.Inventory
        "slow_mover" -> Icons.Outlined.TrendingDown
        "restock_suggestion" -> Icons.Outlined.LocalShipping
        "top_seller" -> Icons.Outlined.TrendingUp
        else -> Icons.Outlined.Inventory
    }
    val timeAgo = remember(alert.createdAt) {
        val diff = System.currentTimeMillis() - alert.createdAt
        when {
            diff < 60_000 -> "Just now"
            diff < 3_600_000 -> "${diff / 60_000} min ago"
            diff < 86_400_000 -> "${diff / 3_600_000}h ago"
            else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(alert.createdAt))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (alert.isRead) Mist else White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (alert.isRead) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                icon,
                contentDescription = alert.type,
                modifier = Modifier.size(20.dp),
                tint = severityColor
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    alert.message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = if (alert.isRead) Ink.copy(alpha = 0.6f) else Ink
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = severityColor.copy(alpha = 0.1f)
                    ) {
                        Text(
                            alert.type.replace("_", " ").replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = severityColor)
                        )
                    }
                    Text(
                        timeAgo,
                        style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                    )
                }
            }
            if (!alert.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Amber, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
