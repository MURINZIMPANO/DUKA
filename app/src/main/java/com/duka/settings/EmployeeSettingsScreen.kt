package com.duka.settings

/**
 * Employee Settings Screen (V5)
 * Leaner version for Employee role — no budget or AI suggestion settings.
 *
 * Sections: Account (name, code, shop, logout), Appearance, Notifications, About
 */

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.DukaApplication
import com.duka.app.R
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.session.SessionManager
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeSettingsScreen(
    sessionManager: SessionManager,
    businessRepository: BusinessRepository,
    employeeName: String,
    onBack: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onLogout: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // Collect data
    val businessId by sessionManager.currentBusinessId.collectAsState(initial = 0L)
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val employeeCode by sessionManager.currentUserName.collectAsState(initial = "")

    // Settings state
    var soundEnabled by remember { mutableStateOf(true) }
    var saleConfirmationSound by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        soundEnabled = true // default
        saleConfirmationSound = true // default
    }

    // Sheets and dialogs
    var showThemeSheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var themeMode by remember { mutableStateOf("system") }

    LaunchedEffect(Unit) {
        themeMode = sessionManager.getThemeMode()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            employeeName.ifBlank { "Employee" },
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = White.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        )
                        Text("Settings", color = White, style = MaterialTheme.typography.headlineMedium)
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // ── Section: Account ─────────────────────────────────────
            item { SectionHeader("Account") }

            item {
                SettingsRow(
                    icon = Icons.Outlined.AccountCircle,
                    title = "Name",
                    subtitle = employeeName.ifBlank { "Employee" }
                )
            }

            item {
                // Employee code — read-only, with copy button
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Outlined.Badge, contentDescription = null, tint = Forest, modifier = Modifier.size(24.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Employee code", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                            Text(
                                employeeCode.ifBlank { "—" },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            )
                        }
                        IconButton(onClick = {
                            scope.launch {
                                clipboardManager.setText(AnnotatedString(employeeCode))
                            }
                        }) {
                            Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy code", tint = Forest, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            item {
                SettingsRow(
                    icon = Icons.Outlined.Storefront,
                    title = "Shop name",
                    subtitle = business?.name ?: "—"
                )
            }

            item {
                SettingsRow(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    iconTint = Clay,
                    title = stringResource(R.string.logout),
                    subtitle = "Log out of this account",
                    onClick = { showLogoutDialog = true }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Section: Appearance ──────────────────────────────────
            item { SectionHeader("Appearance") }

            item {
                SettingsRow(
                    icon = Icons.Outlined.DarkMode,
                    title = "Theme",
                    subtitle = when (themeMode) {
                        "light" -> "Light"
                        "dark" -> "Dark"
                        else -> "System default"
                    },
                    onClick = { showThemeSheet = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Outlined.Language,
                    title = stringResource(R.string.field_language),
                    subtitle = "English",
                    onClick = { showLanguageSheet = true }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Section: Notifications ───────────────────────────────
            item { SectionHeader("Notifications") }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Outlined.VolumeUp, contentDescription = null, tint = Forest, modifier = Modifier.size(24.dp))
                        Text("Sound", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), modifier = Modifier.weight(1f))
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { soundEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Forest,
                                checkedThumbColor = White,
                                uncheckedTrackColor = Mist
                            )
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Outlined.Badge, contentDescription = null, tint = Forest, modifier = Modifier.size(24.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("New sale confirmation sound", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                            Text("Play a tone when a sale is confirmed", style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                        }
                        Switch(
                            checked = saleConfirmationSound,
                            onCheckedChange = { saleConfirmationSound = it },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Forest,
                                checkedThumbColor = White,
                                uncheckedTrackColor = Mist
                            )
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Section: About ───────────────────────────────────────
            item { SectionHeader("About") }

            item {
                val pkgInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                val versionName = pkgInfo.versionName ?: "1.0"
                @Suppress("DEPRECATION")
                val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    pkgInfo.longVersionCode.toInt()
                } else {
                    pkgInfo.versionCode
                }
                SettingsRow(
                    icon = Icons.Outlined.Info,
                    title = "App version",
                    subtitle = "Duka v$versionName (build $versionCode)"
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    // ── Theme bottom sheet ───────────────────────────────────────────
    if (showThemeSheet) {
        ModalBottomSheet(
            onDismissRequest = { showThemeSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Theme", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))
                listOf("system" to "System default", "light" to "Light", "dark" to "Dark").forEach { (mode, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                themeMode = mode
                                scope.launch { sessionManager.saveThemeMode(mode) }
                                val nightMode = when (mode) {
                                    "light" -> AppCompatDelegate.MODE_NIGHT_NO
                                    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                                }
                                AppCompatDelegate.setDefaultNightMode(nightMode)
                                showThemeSheet = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (themeMode == mode) FontWeight.Bold else FontWeight.Normal,
                                color = if (themeMode == mode) Forest else Ink
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        if (themeMode == mode) {
                            Text("✓", color = Forest, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ── Language bottom sheet ────────────────────────────────────────
    if (showLanguageSheet) {
        val languages = listOf(
            Triple("English", "en", stringResource(R.string.lang_english)),
            Triple("Kinyarwanda", "rw", stringResource(R.string.lang_kinyarwanda)),
            Triple("French", "fr", stringResource(R.string.lang_french))
        )
        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(stringResource(R.string.field_language), style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))
                languages.forEach { (displayName, _, _) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageChange(displayName)
                                showLanguageSheet = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(displayName, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ── Logout dialog ────────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out of Duka?") },
            text = { Text("You'll need your employee code to log back in.") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Log out", color = Clay)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Ink
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: androidx.compose.ui.graphics.Color = Forest,
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                }
            }
        }
    }
}
