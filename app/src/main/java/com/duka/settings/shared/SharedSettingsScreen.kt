package com.duka.settings.shared

/**
 * Unified Settings Screen — Shared base composable with role-specific sections.
 *
 * Sections: Account, Appearance, Notifications (role-dependent), Privacy & Data (Owner/Client),
 *           Chat (Client/Owner), Suggestions & AI (Client), About, Logout
 */

import android.os.Build
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.R
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch

/**
 * SharedSettingsScreen — the single composable entry point for all role settings.
 *
 * @param role "owner" | "employee" | "client"
 * @param viewModel the activity-scoped SettingsViewModel
 * @param onBack navigate back
 * @param onNavigateToSuggestionsHistory client-only: navigate to suggestions history
 * @param onNavigateToDataPrivacy client/owner-only: navigate to data & privacy info screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedSettingsScreen(
    role: String,
    viewModel: SettingsViewModel,
    businessRepository: com.duka.app.data.repository.BusinessRepository,
    employeeRepository: com.duka.app.data.repository.EmployeeRepository? = null,
    onBack: () -> Unit,
    onNavigateToSuggestionsHistory: () -> Unit = {},
    onNavigateToDataPrivacy: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Collect from ViewModel
    val userName by viewModel.currentUserName.collectAsState()
    val businessId by viewModel.currentBusinessId.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val languageTag by viewModel.languageTag.collectAsState()
    val pushNotifications by viewModel.pushNotifications.collectAsState()
    val soundEnabled by viewModel.soundEnabled.collectAsState()
    val promotionsEnabled by viewModel.promotionsEnabled.collectAsState()
    val stockAlerts by viewModel.stockAlerts.collectAsState()
    val slowMoverAlerts by viewModel.slowMoverAlerts.collectAsState()
    val chatNotifications by viewModel.chatNotifications.collectAsState()
    val smartSuggestions by viewModel.smartSuggestions.collectAsState()
    val anonymousFeedback by viewModel.anonymousFeedback.collectAsState()
    val saleConfirmationSound by viewModel.saleConfirmationSound.collectAsState()

    // Business name for employee
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)

    // Employee code (from session user name — stored as name during login)
    val employeeCode = userName

    // Edit profile state
    var showEditProfileSheet by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(userName) }

    // Bottom sheets
    var showThemeSheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }

    // Dialogs
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountStep1 by remember { mutableStateOf(false) }
    var showDeleteAccountStep2 by remember { mutableStateOf(false) }
    var deleteConfirmText by remember { mutableStateOf("") }
    var showClearChatDialog by remember { mutableStateOf(false) }

    val isOwner = role == "owner"
    val isEmployee = role == "employee"
    val isClient = role == "client"

    val languageDisplayName = when (languageTag) {
        "rw" -> "Kinyarwanda"
        "fr" -> "Français"
        else -> "English"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            userName.ifBlank { role.replaceFirstChar { it.uppercase() } },
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
        Box(modifier = Modifier.fillMaxSize().background(Canvas)) {
            DecorativeBackdrop(
                page = DukaPage.SETTINGS,
                modifier = Modifier.fillMaxSize()
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // ══════════════════════════════════════════════════════════════
            // SECTION: Account (all roles)
            // ══════════════════════════════════════════════════════════════
            item { SectionHeader("Account") }

            item {
                SettingsRow(
                    icon = Icons.Outlined.AccountCircle,
                    title = userName.ifBlank { "Profile" },
                    subtitle = null,
                    onClick = {
                        editName = userName
                        showEditProfileSheet = true
                    }
                )
            }

            if (isEmployee) {
                item {
                    // Employee code row with copy
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                val clip = android.content.ClipData.newPlainText("Employee code", employeeCode)
                                clipboard.setPrimaryClip(clip)
                            }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Outlined.Settings, contentDescription = "Copy", tint = Forest, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                item {
                    SettingsRow(
                        icon = Icons.Outlined.Storefront,
                        title = "Shop",
                        subtitle = business?.name ?: "—"
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ══════════════════════════════════════════════════════════════
            // SECTION: Appearance (all roles)
            // ══════════════════════════════════════════════════════════════
            item { SectionHeader("Appearance") }

            item {
                SettingsRow(
                    icon = Icons.Outlined.Palette,
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
                    title = "Language",
                    subtitle = languageDisplayName,
                    onClick = { showLanguageSheet = true }
                )
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ══════════════════════════════════════════════════════════════
            // SECTION: Notifications
            // ══════════════════════════════════════════════════════════════
            item { SectionHeader("Notifications") }

            if (isOwner || isClient) {
                item {
                    SwitchRow(
                        icon = Icons.Outlined.Notifications,
                        title = "Push notifications",
                        checked = pushNotifications,
                        onCheckedChange = { viewModel.setPushNotifications(it) }
                    )
                }
            }

            item {
                SwitchRow(
                    icon = Icons.Outlined.VolumeUp,
                    title = "Sound",
                    checked = soundEnabled,
                    onCheckedChange = { viewModel.setSoundEnabled(it) }
                )
            }

            if (isClient) {
                item {
                    SwitchRow(
                        icon = Icons.Outlined.Campaign,
                        title = "Promotions & offers",
                        checked = promotionsEnabled,
                        onCheckedChange = { viewModel.setPromotionsEnabled(it) }
                    )
                }
            }

            if (isOwner) {
                item {
                    SwitchRow(
                        icon = Icons.Outlined.Inventory,
                        title = "Stock alerts",
                        checked = stockAlerts,
                        onCheckedChange = { viewModel.setStockAlerts(it) }
                    )
                }
                item {
                    SwitchRow(
                        icon = Icons.Outlined.TrendingDown,
                        title = "Slow mover alerts",
                        checked = slowMoverAlerts,
                        onCheckedChange = { viewModel.setSlowMoverAlerts(it) }
                    )
                }
            }

            if (isEmployee) {
                item {
                    SwitchRow(
                        icon = Icons.Outlined.PointOfSale,
                        title = "Sale confirmation sound",
                        subtitle = "Play a tone when a sale is confirmed",
                        checked = saleConfirmationSound,
                        onCheckedChange = { viewModel.setSaleConfirmationSound(it) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ══════════════════════════════════════════════════════════════
            // SECTION: Privacy & Data (Owner and Client)
            // ══════════════════════════════════════════════════════════════
            if (isOwner || isClient) {
                item { SectionHeader("Privacy & Data") }

                if (isClient) {
                    item {
                        SwitchRow(
                            icon = Icons.Outlined.VisibilityOff,
                            title = "Anonymous feedback by default",
                            checked = anonymousFeedback,
                            onCheckedChange = { viewModel.setAnonymousFeedback(it) }
                        )
                    }
                }

                item {
                    SettingsRow(
                        icon = Icons.Outlined.Shield,
                        title = "Data & privacy",
                        subtitle = "What we store and share",
                        onClick = onNavigateToDataPrivacy
                    )
                }

                item {
                    SettingsRow(
                        icon = Icons.Outlined.DeleteForever,
                        iconTint = Clay,
                        title = "Delete my account",
                        onClick = { showDeleteAccountStep1 = true }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            // ══════════════════════════════════════════════════════════════
            // SECTION: Chat (Client and Owner)
            // ══════════════════════════════════════════════════════════════
            if (isClient || isOwner) {
                item { SectionHeader("Chat") }

                item {
                    SwitchRow(
                        icon = Icons.Outlined.ChatBubble,
                        title = "Chat notifications",
                        checked = chatNotifications,
                        onCheckedChange = { viewModel.setChatNotifications(it) }
                    )
                }

                item {
                    SettingsRow(
                        icon = Icons.Outlined.Delete,
                        iconTint = Clay,
                        title = "Clear chat history",
                        subtitle = "Delete your sent messages",
                        onClick = { showClearChatDialog = true }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            // ══════════════════════════════════════════════════════════════
            // SECTION: Suggestions & AI (Client only)
            // ══════════════════════════════════════════════════════════════
            if (isClient) {
                item { SectionHeader("Suggestions & AI") }

                item {
                    SwitchRow(
                        icon = Icons.Outlined.AutoAwesome,
                        title = "Smart suggestions",
                        checked = smartSuggestions,
                        onCheckedChange = { viewModel.setSmartSuggestions(it) }
                    )
                }

                item {
                    SettingsRow(
                        icon = Icons.Outlined.TipsAndUpdates,
                        title = "Manage my suggestions",
                        subtitle = "View and delete past suggestions",
                        onClick = onNavigateToSuggestionsHistory
                    )
                }

                item {
                    SwitchRow(
                        icon = Icons.Outlined.VisibilityOff,
                        title = "Anonymous feedback by default",
                        checked = anonymousFeedback,
                        onCheckedChange = { viewModel.setAnonymousFeedback(it) }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            // ══════════════════════════════════════════════════════════════
            // SECTION: About (all roles)
            // ══════════════════════════════════════════════════════════════
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
                    icon = Icons.Outlined.AccountCircle,
                    title = "App version",
                    subtitle = "Duka v$versionName (build $versionCode)"
                )
            }

            // ══════════════════════════════════════════════════════════════
            // SECTION: Logout (all roles — always last)
            // ══════════════════════════════════════════════════════════════
            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLogoutDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null,
                            tint = Clay,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Log out",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = Clay
                            )
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // ── Theme bottom sheet ────────────────────────────────────────────
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
                                viewModel.setThemeMode(mode)
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

    // ── Language bottom sheet ─────────────────────────────────────────
    if (showLanguageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Language", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))
                listOf(
                    "🇷🇼 Kinyarwanda" to "Kinyarwanda",
                    "🇬🇧 English" to "English",
                    "🇫🇷 Français" to "French"
                ).forEach { (display, name) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setLanguage(name)
                                showLanguageSheet = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            display,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (languageDisplayName == name) FontWeight.Bold else FontWeight.Normal,
                                color = if (languageDisplayName == name) Forest else Ink
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        if (languageDisplayName == name) {
                            Text("✓", color = Forest, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ── Edit profile bottom sheet ─────────────────────────────────────
    if (showEditProfileSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditProfileSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Edit profile", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))

                if (isEmployee) {
                    Text(
                        "Ask your shop owner to update your name.",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                    )
                } else {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Forest,
                            unfocusedBorderColor = Mist,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )

                    Text(
                        "To change your login credential, contact support.",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                    )

                    TextButton(onClick = {
                        viewModel.updateEditProfileName(editName)
                        viewModel.saveProfileName()
                        showEditProfileSheet = false
                    }) {
                        Text("Save", color = Forest, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // ── Logout confirmation dialog ────────────────────────────────────
    if (showLogoutDialog) {
        val credentialHint = when {
            isEmployee -> "your employee code"
            else -> "your phone number and password"
        }
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out of Duka?") },
            text = { Text("You'll need $credentialHint to log back in.") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout {
                        // Navigation is handled by the caller observing session state
                    }
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

    // ── Delete account step 1 ─────────────────────────────────────────
    if (showDeleteAccountStep1) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountStep1 = false },
            title = { Text("Are you sure?") },
            text = { Text("This will permanently delete your account and all your data.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteAccountStep1 = false
                    showDeleteAccountStep2 = true
                    deleteConfirmText = ""
                }) {
                    Text("Continue", color = Clay)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountStep1 = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ── Delete account step 2 ─────────────────────────────────────────
    if (showDeleteAccountStep2) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountStep2 = false },
            title = { Text("Type DELETE to confirm") },
            text = {
                Column {
                    Text("This action is irreversible.")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deleteConfirmText,
                        onValueChange = { deleteConfirmText = it },
                        label = { Text("Type DELETE") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Clay,
                            unfocusedBorderColor = Mist
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteAccountStep2 = false
                        // Hard deletion of Room data deferred to a background cleanup job
                        // in a future phase — user cannot log back in but data remains locally
                        // until the device is cleared.
                        viewModel.deleteAccount {}
                    },
                    enabled = deleteConfirmText == "DELETE"
                ) {
                    Text("Delete my account", color = Clay)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountStep2 = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ── Clear chat history dialog ─────────────────────────────────────
    if (showClearChatDialog) {
        AlertDialog(
            onDismissRequest = { showClearChatDialog = false },
            title = { Text("Delete all your chat messages?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearChatHistory()
                    showClearChatDialog = false
                }) {
                    Text("Delete", color = Clay)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearChatDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ── Shared composables ──────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Ink.copy(alpha = 0.5f)
        ),
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    iconTint: Color = Forest,
    title: String,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                if (subtitle != null) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(color = Ink.copy(alpha = 0.6f)),
                        maxLines = 1
                    )
                }
            }
            trailing?.invoke()
        }
    }
}

@Composable
private fun SwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Forest, modifier = Modifier.size(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                if (subtitle != null) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(color = Ink.copy(alpha = 0.6f)),
                        maxLines = 1
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Forest,
                    checkedThumbColor = White,
                    uncheckedTrackColor = Mist
                )
            )
        }
    }
}
