package com.duka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.local.entity.ChatMessage
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ChatRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.duka.app.R
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    businessRepository: BusinessRepository,
    chatRepository: ChatRepository,
    onBack: (() -> Unit)? = null,
    onNavigateToSettings: (() -> Unit)? = null
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    val businessId = business?.id ?: 0L

    val messages by chatRepository.getMessages(businessId).collectAsState(initial = emptyList())
    val listState = rememberLazyListState()

    // Auto-scroll to bottom on new messages
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.CHAT,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.chat_title), color = White) },
                navigationIcon = {
                    onBack?.let { back ->
                        IconButton(onClick = back) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                        }
                    }
                },
                actions = {
                    onNavigateToSettings?.let { settings ->
                        IconButton(onClick = settings) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = White)
                        }
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
            // Messages list
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = "No messages",
                                    modifier = Modifier.size(48.dp),
                                    tint = OnSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    stringResource(R.string.chat_empty),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                                )
                                Text(
                                    stringResource(R.string.chat_empty_hint),
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                )
                            }
                        }
                    }
                }

                items(messages) { message ->
                    val isOwner = message.senderRole == "owner"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isOwner) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = if (isOwner) 12.dp else 0.dp,
                                topEnd = if (isOwner) 0.dp else 12.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            ),
                            color = if (isOwner) Forest else White,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    message.senderLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isOwner) White.copy(alpha = 0.7f) else OnSurfaceVariant
                                    )
                                )
                                Text(
                                    message.text,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (isOwner) White else Ink
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Input bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text(stringResource(R.string.chat_input_hint)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Forest,
                            unfocusedBorderColor = Mist,
                            focusedContainerColor = Canvas,
                            unfocusedContainerColor = Canvas
                        )
                    )
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && businessId > 0) {
                                scope.launch {
                                    chatRepository.sendMessage(
                                        ChatMessage(
                                            businessId = businessId,
                                            senderRole = "owner",
                                            senderLabel = "Owner",
                                            text = inputText.trim()
                                        )
                                    )
                                    inputText = ""
                                }
                            }
                        },
                        enabled = inputText.isNotBlank()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.chat_send),
                            tint = if (inputText.isNotBlank()) Forest else OnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
    }
}
