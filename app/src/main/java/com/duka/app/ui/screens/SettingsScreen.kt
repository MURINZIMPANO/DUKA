package com.duka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.R
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguageChange: (String) -> Unit
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }
    val languages = listOf(
        stringResource(R.string.lang_english) to "EN",
        stringResource(R.string.lang_kinyarwanda) to "RW",
        stringResource(R.string.lang_french) to "FR"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.field_language), color = White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Language section
            Text(
                stringResource(R.string.field_language),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { languageMenuExpanded = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Language,
                            contentDescription = null,
                            tint = Forest,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.field_language),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                stringResource(R.string.lang_english),
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = languageMenuExpanded,
                        onDismissRequest = { languageMenuExpanded = false }
                    ) {
                        languages.forEach { (name, code) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    onLanguageChange(name)
                                    languageMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // About section
            Text(
                "About",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = Forest,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                "Duka",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "v1.0 — Retail Operating System for Rwanda",
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsInfoRow("Version", "1.0 (Build 1)")
                    SettingsInfoRow("Min SDK", "26 (Android 8.0)")
                    SettingsInfoRow("Target SDK", "34 (Android 14)")
                    SettingsInfoRow("Languages", "English, Kinyarwanda, French")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Development info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.TrendingUp,
                            contentDescription = null,
                            tint = Amber,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                "Built with",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "Kotlin, Jetpack Compose, Material 3, Room, Hilt",
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
        Text(value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
    }
}
