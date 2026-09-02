package com.duka.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircleOutline
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
import com.duka.app.data.repository.AppNotificationRepository
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeNotificationCentreScreen(
    userId: Long,
    businessId: Long,
    appNotificationRepository: AppNotificationRepository,
    onBack: () -> Unit,
    onNotificationTap: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val notifications by appNotificationRepository.getByUserAndTypes(
        userId, listOf("chat_message", "new_sale")
    ).collectAsState(initial = emptyList())

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
        if (notifications.isEmpty()) {
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
                val grouped = notifications.groupBy { notif ->
                    val date = Instant.ofEpochMilli(notif.createdAt).atZone(ZoneId.systemDefault()).toLocalDate()
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
                    itemsIndexed(items) { index, notif ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay((index * 30).toLong())
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(300)) + slideInVertically(tween(300, easing = FastOutSlowInEasing))
                        ) {
                            NotificationCard(
                                notification = notif,
                                onTap = {
                                    scope.launch { appNotificationRepository.markAsRead(notif.id) }
                                    onNotificationTap(notif.actionRoute)
                                },
                                onMarkRead = {
                                    scope.launch { appNotificationRepository.markAsRead(notif.id) }
                                },
                                onDelete = {
                                    scope.launch { appNotificationRepository.deleteById(notif.id) }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
