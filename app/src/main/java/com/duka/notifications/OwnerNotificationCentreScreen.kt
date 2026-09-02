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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.duka.app.data.local.entity.AppNotification
import com.duka.app.data.local.entity.ProductAlert
import com.duka.app.data.repository.AppNotificationRepository
import com.duka.app.data.repository.ProductAlertRepository
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Owner notification centre — merges ProductAlert and AppNotification rows,
 * grouped by date ("Today", "Yesterday", "Earlier this week").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerNotificationCentreScreen(
    businessId: Long,
    userId: Long,
    productAlertRepository: ProductAlertRepository,
    appNotificationRepository: AppNotificationRepository,
    onBack: () -> Unit,
    onAlertTap: (Long) -> Unit, // productId for edit product
    onNotificationTap: (String) -> Unit // actionRoute
) {
    val scope = rememberCoroutineScope()
    val alerts by productAlertRepository.getByBusiness(businessId).collectAsState(initial = emptyList())
    val notifications by appNotificationRepository.getByUser(userId).collectAsState(initial = emptyList())

    // Convert alerts to notifications for unified display
    val allItems = remember(alerts, notifications) {
        val alertItems = alerts.map { alert ->
            NotificationItem(
                id = "alert_${alert.id}",
                title = alert.type.replace("_", " ").replaceFirstChar { it.uppercase() },
                body = alert.message,
                type = alert.type,
                isRead = alert.isRead,
                createdAt = alert.createdAt,
                actionRoute = "edit_product/${alert.productId}"
            )
        }
        val notifItems = notifications.map { notif ->
            NotificationItem(
                id = "notif_${notif.id}",
                title = notif.title,
                body = notif.body,
                type = notif.type,
                isRead = notif.isRead,
                createdAt = notif.createdAt,
                actionRoute = notif.actionRoute
            )
        }
        (alertItems + notifItems).sortedByDescending { it.createdAt }
    }

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        scope.launch {
                            productAlertRepository.markAllAsRead(businessId)
                            appNotificationRepository.markAllAsRead(userId)
                        }
                    }) {
                        Text("Mark all read", color = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        if (allItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.CheckCircleOutline, contentDescription = null, modifier = Modifier.size(48.dp), tint = Forest)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No notifications yet", style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group by date
                val grouped = allItems.groupBy { item ->
                    val date = Instant.ofEpochMilli(item.createdAt).atZone(ZoneId.systemDefault()).toLocalDate()
                    when (date) {
                        today -> "Today"
                        yesterday -> "Yesterday"
                        else -> "Earlier"
                    }
                }

                grouped.forEach { (dateLabel, items) ->
                    item {
                        Text(
                            dateLabel,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariant
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    itemsIndexed(items) { index, item ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay((index * 30).toLong())
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(300)) + slideInVertically(tween(300, easing = FastOutSlowInEasing))
                        ) {
                            NotificationItemCard(
                                item = item,
                                onTap = {
                                    if (item.id.startsWith("alert_")) {
                                        val productId = item.actionRoute.removePrefix("edit_product/").toLongOrNull() ?: 0L
                                        onAlertTap(productId)
                                    } else {
                                        onNotificationTap(item.actionRoute)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: Long,
    val actionRoute: String
)

@Composable
private fun NotificationItemCard(
    item: NotificationItem,
    onTap: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onTap() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isRead) com.duka.app.ui.theme.Mist else White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isRead) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = com.duka.app.ui.theme.Ink.copy(alpha = if (item.isRead) 0.6f else 1f)
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    item.body,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = com.duka.app.ui.theme.Ink.copy(alpha = 0.7f)
                    ),
                    maxLines = 2
                )
            }
            if (!item.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(com.duka.app.ui.theme.Amber, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
