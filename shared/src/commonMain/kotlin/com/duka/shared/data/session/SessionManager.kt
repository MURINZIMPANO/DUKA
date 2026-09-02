package com.duka.shared.data.session

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import com.duka.shared.data.repository.UserRepository
import com.duka.shared.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * SessionManager — Multiplatform Settings-backed session persistence.
 * Uses synchronous settings API with polling for flow behavior.
 */
@OptIn(ExperimentalSettingsApi::class)
class SessionManager(
    private val settings: ObservableSettings,
    private val userRepository: UserRepository
) {
    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_BUSINESS_ID = "business_id"
        private const val KEY_ROLE = "role"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_THEME = "theme_mode"
        private const val KEY_PUSH_NOTIFICATIONS = "push_notifications"
        private const val KEY_SOUND = "sound_enabled"
        private const val KEY_PROMOTIONS = "promotions_enabled"
        private const val KEY_CHAT_NOTIFICATIONS = "chat_notifications"
        private const val KEY_SMART_SUGGESTIONS = "smart_suggestions"
        private const val KEY_ANONYMOUS_FEEDBACK = "anonymous_feedback_default"
        private const val KEY_SALE_CONFIRMATION_SOUND = "sale_confirmation_sound"
        private const val KEY_STOCK_ALERTS = "stock_alerts"
        private const val KEY_SLOW_MOVER_ALERTS = "slow_mover_alerts"
    }

    // Internal state flows
    private val _currentUserId = MutableStateFlow(settings[KEY_USER_ID, 0L])
    private val _currentBusinessId = MutableStateFlow(settings[KEY_BUSINESS_ID, 0L])
    private val _currentRole = MutableStateFlow<String?>(settings.get(KEY_ROLE))
    private val _currentUserName = MutableStateFlow(settings[KEY_USER_NAME] ?: "")

    val currentUserId: Flow<Long> get() = _currentUserId
    val currentBusinessId: Flow<Long> get() = _currentBusinessId
    val currentRole: Flow<String?> get() = _currentRole
    val currentUserName: Flow<String> get() = _currentUserName
    val hasSession: Flow<Boolean> get() = _currentRole.map { it != null }
    val languageCode: Flow<String> get() = MutableStateFlow(settings[KEY_LANGUAGE] ?: "en")

    val themeMode: Flow<String> get() = MutableStateFlow(settings[KEY_THEME] ?: "system")
    val pushNotifications: Flow<Boolean> get() = MutableStateFlow(settings[KEY_PUSH_NOTIFICATIONS, true])
    val soundEnabled: Flow<Boolean> get() = MutableStateFlow(settings[KEY_SOUND, true])
    val promotionsEnabled: Flow<Boolean> get() = MutableStateFlow(settings[KEY_PROMOTIONS, true])
    val chatNotifications: Flow<Boolean> get() = MutableStateFlow(settings[KEY_CHAT_NOTIFICATIONS, true])
    val smartSuggestions: Flow<Boolean> get() = MutableStateFlow(settings[KEY_SMART_SUGGESTIONS, true])
    val anonymousFeedbackDefault: Flow<Boolean> get() = MutableStateFlow(settings[KEY_ANONYMOUS_FEEDBACK, false])
    val saleConfirmationSound: Flow<Boolean> get() = MutableStateFlow(settings[KEY_SALE_CONFIRMATION_SOUND, true])
    val stockAlerts: Flow<Boolean> get() = MutableStateFlow(settings[KEY_STOCK_ALERTS, true])
    val slowMoverAlerts: Flow<Boolean> get() = MutableStateFlow(settings[KEY_SLOW_MOVER_ALERTS, true])

    suspend fun saveSession(userId: Long, businessId: Long, role: String, userName: String) {
        settings[KEY_USER_ID] = userId
        settings[KEY_BUSINESS_ID] = businessId
        settings[KEY_ROLE] = role
        settings[KEY_USER_NAME] = userName
        _currentUserId.value = userId
        _currentBusinessId.value = businessId
        _currentRole.value = role
        _currentUserName.value = userName
    }

    suspend fun updateBusinessId(businessId: Long) {
        settings[KEY_BUSINESS_ID] = businessId
        _currentBusinessId.value = businessId
    }

    suspend fun clearSession() {
        settings.remove(KEY_USER_ID)
        settings.remove(KEY_BUSINESS_ID)
        settings.remove(KEY_ROLE)
        settings.remove(KEY_USER_NAME)
        _currentUserId.value = 0L
        _currentBusinessId.value = 0L
        _currentRole.value = null
        _currentUserName.value = ""
    }

    suspend fun saveLanguage(code: String) { settings[KEY_LANGUAGE] = code }
    suspend fun getLanguageCode(): String = settings[KEY_LANGUAGE] ?: "en"

    suspend fun saveThemeMode(mode: String) { settings[KEY_THEME] = mode }
    suspend fun savePushNotifications(enabled: Boolean) { settings[KEY_PUSH_NOTIFICATIONS] = enabled }
    suspend fun saveSoundEnabled(enabled: Boolean) { settings[KEY_SOUND] = enabled }
    suspend fun savePromotionsEnabled(enabled: Boolean) { settings[KEY_PROMOTIONS] = enabled }
    suspend fun saveChatNotifications(enabled: Boolean) { settings[KEY_CHAT_NOTIFICATIONS] = enabled }
    suspend fun saveSmartSuggestions(enabled: Boolean) { settings[KEY_SMART_SUGGESTIONS] = enabled }
    suspend fun saveAnonymousFeedbackDefault(enabled: Boolean) { settings[KEY_ANONYMOUS_FEEDBACK] = enabled }
    suspend fun saveSaleConfirmationSound(enabled: Boolean) { settings[KEY_SALE_CONFIRMATION_SOUND] = enabled }
    suspend fun saveStockAlerts(enabled: Boolean) { settings[KEY_STOCK_ALERTS] = enabled }
    suspend fun saveSlowMoverAlerts(enabled: Boolean) { settings[KEY_SLOW_MOVER_ALERTS] = enabled }

    suspend fun getCurrentUser(): User? {
        val uid = settings[KEY_USER_ID, 0L]
        return if (uid > 0) userRepository.getById(uid) else null
    }

    suspend fun getBusinessId(): Long = settings[KEY_BUSINESS_ID, 0L]
    suspend fun getRole(): String? = settings.get(KEY_ROLE)
    suspend fun getThemeMode(): String = settings[KEY_THEME] ?: "system"
    suspend fun getSmartSuggestions(): Boolean = settings[KEY_SMART_SUGGESTIONS, true]
    suspend fun getAnonymousFeedbackDefault(): Boolean = settings[KEY_ANONYMOUS_FEEDBACK, false]
    suspend fun getPromotionsEnabled(): Boolean = settings[KEY_PROMOTIONS, true]
    suspend fun getChatNotifications(): Boolean = settings[KEY_CHAT_NOTIFICATIONS, true]
    suspend fun getSaleConfirmationSound(): Boolean = settings[KEY_SALE_CONFIRMATION_SOUND, true]
    suspend fun getPushNotifications(): Boolean = settings[KEY_PUSH_NOTIFICATIONS, true]
    suspend fun getSoundEnabled(): Boolean = settings[KEY_SOUND, true]
}
