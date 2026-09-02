package com.duka.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.local.entity.AppNotification
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Shared notification card used in all three centres (Owner, Client, Employee).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationCard(
    notification: AppNotification,
    onTap: () -> Unit,
    onMarkRead: () -> Unit,
    onDelete: () -> Unit
) {
    val bgColor = if (notification.isRead) Mist else White
    val textAlpha = if (notification.isRead) 0.6f else 1f
    val borderColor = if (notification.isRead) Color.Transparent else Mist

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onTap,
                onLongClick = onMarkRead
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            val icon = iconForType(notification.type)
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = colorForType(notification.type)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    notification.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Ink.copy(alpha = textAlpha)
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    notification.body,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Ink.copy(alpha = 0.7f * textAlpha)
                    ),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colorForType(notification.type).copy(alpha = 0.1f)
                    ) {
                        Text(
                            notification.type.replace("_", " ").replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = colorForType(notification.type))
                        )
                    }
                    Text(
                        timeAgo(notification.createdAt),
                        style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                    )
                }
            }
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Amber, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

private fun iconForType(type: String): ImageVector = when (type) {
    "low_stock", "restock" -> Icons.Outlined.Inventory
    "slow_mover" -> Icons.Outlined.TrendingDown
    "top_seller", "new_sale" -> Icons.Outlined.TrendingUp
    else -> Icons.Outlined.Notifications
}

private fun colorForType(type: String): Color = when (type) {
    "low_stock" -> Clay
    "budget_over" -> Clay
    "slow_mover" -> Amber
    "budget_warning" -> Amber
    "restock" -> Amber
    else -> Forest
}

private fun timeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000} min ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        diff < 604_800_000 -> "${diff / 86_400_000}d ago"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}
