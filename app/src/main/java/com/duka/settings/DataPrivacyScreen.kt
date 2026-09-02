package com.duka.settings

/**
 * Data & Privacy Screen
 * Static informational screen explaining what data is stored locally,
 * what is mocked, and what would be shared with a real backend.
 * No ViewModel needed — purely informational.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataPrivacyScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Privacy",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = White.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        )
                        Text("Data & privacy", color = White, style = MaterialTheme.typography.headlineMedium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(
                            Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = Forest
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Your data stays on your device",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Duka currently stores all your data locally on this device. Nothing is sent to a server or shared with third parties.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            item {
                PrivacySection(
                    title = "What we store locally",
                    items = listOf(
                        "Your account details (name, phone/email)",
                        "Your products, sales, and inventory",
                        "Chat messages with your shop",
                        "Purchase history and budget goals",
                        "Feedback and suggestions you submit",
                        "App preferences (theme, language, notifications)"
                    )
                )
            }

            item {
                PrivacySection(
                    title = "What is mocked (demo data)",
                    items = listOf(
                        "Business ratings and distances",
                        "Credit scores and lending readiness",
                        "Government tax engine data",
                        "Wholesaler marketplace listings",
                        "EBM receipt numbers",
                        "Push notifications (toggle exists but no real FCM)"
                    )
                )
            }

            item {
                PrivacySection(
                    title = "What would be shared with a real backend",
                    items = listOf(
                        "Account registration and login",
                        "Product and sales data for cloud sync",
                        "Chat messages (end-to-end encrypted)",
                        "Feedback and complaints (sent to government channels)",
                        "EBM receipts (sent to tax authority)",
                        "Push notification tokens (FCM)"
                    )
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "In this phase",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Duka is a local-first application. All data lives on your device and is wiped if you uninstall the app. No real government transmission, payment processing, or cloud sync is active. This will change as the platform matures.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun PrivacySection(title: String, items: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            items.forEachIndexed { index, item ->
                Text(
                    "• $item",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                )
                if (index < items.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
