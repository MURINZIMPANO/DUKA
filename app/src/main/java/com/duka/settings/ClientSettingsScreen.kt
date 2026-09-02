package com.duka.settings

/**
 * Client/Shopper Settings Screen
 * Full settings for the Client role, accessible from the profile icon on Client Discover.
 *
 * Sections: Account, Appearance, Notifications, Chat, Suggestions & AI, Privacy, About
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
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.R
import com.duka.app.DukaApplication
import com.duka.app.data.repository.ChatRepository
import com.duka.app.data.repository.FeedbackReportRepository
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
fun ClientSettingsScreen(
    sessionManager: SessionManager,
    chatRepository: ChatRepository,
    feedbackReportRepository: FeedbackReportRepository,
    onBack: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onNavigateToSuggestionsHistory: () -> Unit,
    onNavigateToDataPrivacy: () -> Unit,
    onLogout: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Collect settings from DataStore
    val userName by sessionManager.currentUserName.collectAsState(initial = "")
    val currentRole by sessionManager.currentRole.collectAsState(initial = null)
    val businessId by sessionManager.currentBusinessId.collectAsState(initial = 0L)

    var themeMode by remember { mutableStateOf("system") }
    var pushNotifications by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var promotionsEnabled by remember { mutableStateOf(true) }
    var chatNotifications by remember { mutableStateOf(true) }
    var smartSuggestions by remember { mutableStateOf(true) }
    var anonymousFeedback by remember { mutableStateOf(false) }
    var currentLangTag by remember { mutableStateOf("en") }

    LaunchedEffect(Unit) {
        themeMode = sessionManager.getThemeMode()
        pushNotifications = sessionManager.getPromotionsEnabled()
        soundEnabled = true
        promotionsEnabled = sessionManager.getPromotionsEnabled()
        chatNotifications = sessionManager.getChatNotifications()
        smartSuggestions = sessionManager.getSmartSuggestions()
        anonymousFeedback = sessionManager.getAnonymousFeedbackDefault()
        currentLangTag = sessionManager.getLanguageCode()
    }

    // Sheets and dialogs
    var showThemeSheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showClearChatDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            userName.ifBlank { "Shopper" },
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
                    title = "Profile",
                    subtitle = userName.ifBlank { "Shopper" },
                    onClick = { /* Edit Profile bottom sheet — name editable, phone/email display-only */ }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    iconTint = Clay,
                    title = stringResource(R.string.logout),
                    subtitle = "Log out of Duka",
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
                    subtitle = when (currentLangTag) {
                        "rw" -> "Kinyarwanda"
                        "fr" -> "Français"
                        else -> "English"
                    },
                    onClick = { showLanguageSheet = true }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Section: Notifications ───────────────────────────────
            item { SectionHeader("Notifications") }

            item {
                SwitchRow(
                    icon = Icons.Outlined.Notifications,
                    title = "Push notifications",
                    checked = pushNotifications,
                    onCheckedChange = {
                        pushNotifications = it
                        scope.launch { sessionManager.savePushNotifications(it) }
                        // TODO: wire to FCM token registration/deregistration once backend is added
                    }
                )
            }

            item {
                SwitchRow(
                    icon = Icons.Outlined.VolumeUp,
                    title = "Sound",
                    checked = soundEnabled,
                    onCheckedChange = {
                        soundEnabled = it
                        scope.launch { sessionManager.saveSoundEnabled(it) }
                    }
                )
            }

            item {
                SwitchRow(
                    icon = Icons.Outlined.Campaign,
                    title = "Promotions & offers",
                    checked = promotionsEnabled,
                    onCheckedChange = {
                        promotionsEnabled = it
                        scope.launch { sessionManager.savePromotionsEnabled(it) }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Section: Chat ────────────────────────────────────────
            item { SectionHeader("Chat") }

            item {
                SwitchRow(
                    icon = Icons.Outlined.ChatBubble,
                    title = "Chat notifications",
                    checked = chatNotifications,
                    onCheckedChange = {
                        chatNotifications = it
                        scope.launch { sessionManager.saveChatNotifications(it) }
                    }
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

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Section: Suggestions & AI ────────────────────────────
            item { SectionHeader("Suggestions & AI") }

            item {
                SwitchRow(
                    icon = Icons.Outlined.AutoAwesome,
                    title = "Smart suggestions",
                    checked = smartSuggestions,
                    onCheckedChange = {
                        smartSuggestions = it
                        scope.launch { sessionManager.saveSmartSuggestions(it) }
                        // TODO: replace rule-based tips with a real ML/LLM model once backend is ready — keep this toggle in place as the user-facing control
                    }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Outlined.TipsAndUpdates,
                    title = "Manage my suggestions",
                    subtitle = "View and delete past suggestions",
                    onClick = onNavigateToSuggestionsHistory,
                    showChevron = true
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Section: Privacy ─────────────────────────────────────
            item { SectionHeader("Privacy") }

            item {
                SwitchRow(
                    icon = Icons.Outlined.VisibilityOff,
                    title = "Anonymous feedback by default",
                    checked = anonymousFeedback,
                    onCheckedChange = {
                        anonymousFeedback = it
                        scope.launch { sessionManager.saveAnonymousFeedbackDefault(it) }
                    }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Outlined.Shield,
                    title = "Data & privacy",
                    subtitle = "What we store and share",
                    onClick = onNavigateToDataPrivacy,
                    showChevron = true
                )
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
                                // Apply theme via AppCompatDelegate
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
                        Text(
                            displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ── Logout confirmation dialog ───────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out of Duka?") },
            text = { Text("You'll need your phone number and password to log back in.") },
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

    // ── Clear chat history dialog ────────────────────────────────────
    if (showClearChatDialog) {
        AlertDialog(
            onDismissRequest = { showClearChatDialog = false },
            title = { Text("Delete all your chat messages?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        chatRepository.deleteClientMessages(businessId)
                    }
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
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color = Forest,
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    showChevron: Boolean = false
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
            if (showChevron) {
                Icon(
                    Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SwitchRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
            Icon(icon, contentDescription = null, tint = Forest, modifier = Modifier.size(24.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.weight(1f)
            )
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
