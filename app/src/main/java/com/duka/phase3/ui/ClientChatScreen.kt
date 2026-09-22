package com.duka.phase3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.ClientChatMessage
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch

/**
 * Phase 3 — Client ↔ Owner conversation, reached from a Shop Profile's Message
 * button. Mirrors the existing V1/V2 ChatScreen patterns (same bubble shapes,
 * same tokens) with Phase 3 data. Polling keeps both devices in sync; pending
 * sends show a visible "not delivered yet" marker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientChatScreen(
    repository: com.duka.phase3.data.Phase3Repository,
    service: com.duka.phase3.chat.ClientChatService,
    shopRemoteId: String,
    clientUserId: Long,
    senderRole: String,        // "client" (from Shop Profile) or "owner" (from chat list)
    senderName: String,
    onBack: () -> Unit
) {
    val conversationId = remember(shopRemoteId, clientUserId) {
        service.conversationId(shopRemoteId, clientUserId)
    }
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    val sendState by service.sendState.collectAsState()

    // Shop name resolved from the Explore cache (offline-safe).
    val shop by repository.observeShop(shopRemoteId).collectAsState(initial = null)
    val shopName = shop?.name ?: "Shop"

    val messages by service.observeConversation(conversationId).collectAsState(initial = emptyList())
    val listState = rememberLazyListState()

    // Realtime-ish: poll while this screen is open.
    LaunchedEffect(conversationId) {
        service.startPolling(this, conversationId, shopRemoteId, clientUserId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(shopName, color = White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (senderRole == "client") "Chat with this shop" else "Client conversation",
                            color = White.copy(alpha = 0.8f), fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(8.dp)
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Message…", color = OnSurfaceVariant) },
                    maxLines = 3,
                    shape = RoundedCornerShape(20.dp),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist
                    )
                )
                IconButton(
                    onClick = {
                        val text = input.trim()
                        if (text.isNotEmpty()) {
                            scope.launch {
                                service.sendMessage(
                                    conversationId = conversationId,
                                    shopRemoteId = shopRemoteId,
                                    clientUserId = clientUserId,
                                    senderRole = senderRole,
                                    senderLabel = senderName,
                                    text = text
                                )
                            }
                            input = ""
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Forest)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (sendState.lastError != null) {
                Text(
                    sendState.lastError!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(com.duka.app.ui.theme.Clay.copy(alpha = 0.12f))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    color = com.duka.app.ui.theme.Clay,
                    fontSize = 12.sp
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Text(
                            "Say hello — your message goes to this shop's owner.",
                            color = OnSurfaceVariant, fontSize = 13.sp
                        )
                    }
                }
                items(messages, key = { it.remoteId.ifEmpty { "local-${it.timestamp}" } }) { msg ->
                    ChatBubble(msg, isMine = msg.senderRole == senderRole)
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(msg: ClientChatMessage, isMine: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    if (isMine) Forest else White,
                    RoundedCornerShape(
                        topStart = 14.dp, topEnd = 14.dp,
                        bottomStart = if (isMine) 14.dp else 4.dp,
                        bottomEnd = if (isMine) 4.dp else 14.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(msg.text, color = if (isMine) White else Ink, fontSize = 14.sp)
            Spacer(Modifier.height(2.dp))            Text(
                (if (isMine) "You" else msg.senderLabel) +
                        if (msg.pendingPush) " · not delivered yet" else "",
                color = if (isMine) White.copy(alpha = 0.7f) else OnSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}

