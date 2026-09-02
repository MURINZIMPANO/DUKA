package com.duka.settings.shared

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duka.app.data.local.entity.User
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ChatRepository
import com.duka.app.data.repository.UserRepository
import com.duka.app.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SettingsViewModel — Single source of truth for all settings across roles.
 *
 * Activity-scoped Hilt ViewModel (retained across screen transitions within the same role session).
 * Reads/writes all preferences from/to DataStore via SessionManager.
 * Exposes them as StateFlows for the SharedSettingsScreen composable.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val businessRepository: BusinessRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    // ── Session flows (derived from DataStore) ────────────────────────

    val currentUserName: StateFlow<String> = sessionManager.currentUserName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val currentRole: StateFlow<String?> = sessionManager.currentRole
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentBusinessId: StateFlow<Long> = sessionManager.currentBusinessId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val currentUserId: StateFlow<Long> = sessionManager.currentUserId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    // ── Theme ─────────────────────────────────────────────────────────

    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    // ── Notification toggles ──────────────────────────────────────────

    private val _pushNotifications = MutableStateFlow(true)
    val pushNotifications: StateFlow<Boolean> = _pushNotifications.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _promotionsEnabled = MutableStateFlow(true)
    val promotionsEnabled: StateFlow<Boolean> = _promotionsEnabled.asStateFlow()

    private val _stockAlerts = MutableStateFlow(true)
    val stockAlerts: StateFlow<Boolean> = _stockAlerts.asStateFlow()

    private val _slowMoverAlerts = MutableStateFlow(true)
    val slowMoverAlerts: StateFlow<Boolean> = _slowMoverAlerts.asStateFlow()

    private val _chatNotifications = MutableStateFlow(true)
    val chatNotifications: StateFlow<Boolean> = _chatNotifications.asStateFlow()

    private val _smartSuggestions = MutableStateFlow(true)
    val smartSuggestions: StateFlow<Boolean> = _smartSuggestions.asStateFlow()

    private val _anonymousFeedback = MutableStateFlow(false)
    val anonymousFeedback: StateFlow<Boolean> = _anonymousFeedback.asStateFlow()

    private val _saleConfirmationSound = MutableStateFlow(true)
    val saleConfirmationSound: StateFlow<Boolean> = _saleConfirmationSound.asStateFlow()

    // ── Current language tag ──────────────────────────────────────────

    private val _languageTag = MutableStateFlow("en")
    val languageTag: StateFlow<String> = _languageTag.asStateFlow()

    // ── Edit profile state ────────────────────────────────────────────

    private val _editProfileName = MutableStateFlow("")
    val editProfileName: StateFlow<String> = _editProfileName.asStateFlow()

    private val _isSavingProfile = MutableStateFlow(false)
    val isSavingProfile: StateFlow<Boolean> = _isSavingProfile.asStateFlow()

    init {
        viewModelScope.launch {
            _themeMode.value = sessionManager.getThemeMode()
            _languageTag.value = sessionManager.getLanguageCode()
            _pushNotifications.value = sessionManager.getPromotionsEnabled()
            _soundEnabled.value = true
            _promotionsEnabled.value = sessionManager.getPromotionsEnabled()
            _chatNotifications.value = sessionManager.getChatNotifications()
            _smartSuggestions.value = sessionManager.getSmartSuggestions()
            _anonymousFeedback.value = sessionManager.getAnonymousFeedbackDefault()
            _saleConfirmationSound.value = sessionManager.getSaleConfirmationSound()
            _editProfileName.value = sessionManager.currentUserName.firstOrNull() ?: ""
        }
    }

    // ── Theme mutators ────────────────────────────────────────────────

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            _themeMode.value = mode
            sessionManager.saveThemeMode(mode)
            val nightMode = when (mode) {
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    // ── Language mutators ─────────────────────────────────────────────

    fun setLanguage(displayName: String) {
        viewModelScope.launch {
            val tag = com.duka.app.DukaApplication.languageTagFromName(displayName)
            _languageTag.value = tag
            sessionManager.saveLanguage(tag)
            com.duka.app.DukaApplication.applyLocale(tag)
        }
    }

    // ── Notification toggle mutators ──────────────────────────────────

    fun setPushNotifications(enabled: Boolean) {
        _pushNotifications.value = enabled
        viewModelScope.launch { sessionManager.savePushNotifications(enabled) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        _soundEnabled.value = enabled
        viewModelScope.launch { sessionManager.saveSoundEnabled(enabled) }
    }

    fun setPromotionsEnabled(enabled: Boolean) {
        _promotionsEnabled.value = enabled
        viewModelScope.launch { sessionManager.savePromotionsEnabled(enabled) }
    }

    fun setStockAlerts(enabled: Boolean) {
        _stockAlerts.value = enabled
        viewModelScope.launch { sessionManager.saveStockAlerts(enabled) }
    }

    fun setSlowMoverAlerts(enabled: Boolean) {
        _slowMoverAlerts.value = enabled
        viewModelScope.launch { sessionManager.saveSlowMoverAlerts(enabled) }
    }

    fun setChatNotifications(enabled: Boolean) {
        _chatNotifications.value = enabled
        viewModelScope.launch { sessionManager.saveChatNotifications(enabled) }
    }

    fun setSmartSuggestions(enabled: Boolean) {
        _smartSuggestions.value = enabled
        viewModelScope.launch { sessionManager.saveSmartSuggestions(enabled) }
    }

    fun setAnonymousFeedback(enabled: Boolean) {
        _anonymousFeedback.value = enabled
        viewModelScope.launch { sessionManager.saveAnonymousFeedbackDefault(enabled) }
    }

    fun setSaleConfirmationSound(enabled: Boolean) {
        _saleConfirmationSound.value = enabled
        viewModelScope.launch { sessionManager.saveSaleConfirmationSound(enabled) }
    }

    // ── Profile editing ───────────────────────────────────────────────

    fun updateEditProfileName(name: String) {
        _editProfileName.value = name
    }

    fun saveProfileName() {
        val name = _editProfileName.value.trim()
        if (name.isBlank()) return
        viewModelScope.launch {
            _isSavingProfile.value = true
            try {
                val userId = currentUserId.value
                if (userId > 0) {
                    val user = userRepository.getById(userId)
                    if (user != null) {
                        userRepository.updateName(userId, name)
                        sessionManager.saveSession(
                            userId = userId,
                            businessId = user.businessId ?: 0L,
                            role = user.role,
                            userName = name
                        )
                        _currentUserNameInternal.value = name
                    }
                }
            } finally {
                _isSavingProfile.value = false
            }
        }
    }

    private val _currentUserNameInternal = MutableStateFlow("")

    // ── Account deletion ──────────────────────────────────────────────

    fun deleteAccount(onDone: () -> Unit) {
        viewModelScope.launch {
            val userId = currentUserId.value
            if (userId > 0) {
                userRepository.softDelete(userId)
                sessionManager.clearSession()
                onDone()
            }
        }
    }

    // ── Logout ────────────────────────────────────────────────────────

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onDone()
        }
    }

    // ── Chat clearing ─────────────────────────────────────────────────

    fun clearChatHistory() {
        viewModelScope.launch {
            val bizId = currentBusinessId.value
            if (bizId > 0) {
                chatRepository.deleteClientMessages(bizId)
            }
        }
    }
}
