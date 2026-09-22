package com.duka.phase3.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.ClientChatMessage
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White

/**
 * Phase 3 — Owner-side chat list: every active client conversation with its
 * latest message, so an owner doesn't hunt for which client messaged them.
 * Local cache renders instantly; a poll refresh pulls new messages.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerClientChatListScreen(
    repository: com.duka.phase3.data.Phase3Repository,
    service: com.duka.phase3.chat.ClientChatService,
    onBack: () -> Unit,
    onOpenConversation: (shopRemoteId: String, shopName: String, clientUserId: Long) -> Unit
) {
    val allMessages by repository.observeAllClientMessages().collectAsState(initial = emptyList())
    val shops by repository.observeAllShops().collectAsState(initial = emptyList())

    // Shop names resolved from the Explore cache; falls back to the sender label.
    val shopNames = remember(shops) { shops.associate { it.remoteId to it.name } }

    // Group into conversations, keep the latest message per conversation.
    val conversations = remember(allMessages) {
        allMessages
            .groupBy { it.conversationId }
            .map { (_, msgs) -> msgs.maxByOrNull { it.timestamp }!! }
            .sortedByDescending { it.timestamp }
    }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            TopAppBar(
                title = { Text("Client chats", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        if (conversations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.ChatBubbleOutline, contentDescription = null,
                        tint = OnSurfaceVariant, modifier = Modifier.size(44.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No client messages yet.\nWhen a client messages your shop, it appears here.",
                        color = OnSurfaceVariant, fontSize = 13.sp, lineHeight = 18.sp
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
                items(conversations, key = { it.conversationId }) { latest ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOpenConversation(
                                    latest.shopId,
                                    shopNames[latest.shopId] ?: shopNameFor(latest),
                                    latest.clientUserId
                                )
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Forest.copy(alpha = 0.12f), RoundedCornerShape(20.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    (shopNames[latest.shopId] ?: shopNameFor(latest)).take(1).uppercase(),
                                    color = Forest, fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    shopNames[latest.shopId] ?: shopNameFor(latest),
                                    fontWeight = FontWeight.Bold, fontSize = 14.sp,
                                    color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    latest.text,
                                    fontSize = 12.sp, color = OnSurfaceVariant,
                                    maxLines = 1, overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                formatTime(latest.timestamp),
                                fontSize = 11.sp, color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Falls back to the message's sender label when the shop isn't in the Explore
 * cache (e.g. before first sync).
 */
internal fun shopNameFor(msg: ClientChatMessage): String = msg.senderLabel.ifBlank { "Client" }

internal fun formatTime(epochMillis: Long): String {
    if (epochMillis <= 0) return ""
    val fmt = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    return fmt.format(java.util.Date(epochMillis))
}
