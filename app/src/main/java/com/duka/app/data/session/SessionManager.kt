package com.duka.app.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.duka.app.data.local.dao.UserDao
import com.duka.app.data.local.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SessionManager — DataStore-backed session persistence.
 *
 * Single source of truth for which user is logged in, their role, and their business.
 * Read at app launch to decide the start destination.
 *
 * Pattern: optimistic UI updates + background persistence.
 * Navigation state is always derived from this session, never from transient local state.
 */
@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao
) {
    companion object {
        private val KEY_USER_ID = longPreferencesKey("user_id")
        private val KEY_BUSINESS_ID = longPreferencesKey("business_id")
        private val KEY_ROLE = stringPreferencesKey("role")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_LANGUAGE = stringPreferencesKey("language")

        // Settings keys — Client & Employee
        private val KEY_THEME = stringPreferencesKey("theme_mode")       // "system" | "light" | "dark"
        private val KEY_PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
        private val KEY_SOUND = booleanPreferencesKey("sound_enabled")
        private val KEY_PROMOTIONS = booleanPreferencesKey("promotions_enabled")
        private val KEY_CHAT_NOTIFICATIONS = booleanPreferencesKey("chat_notifications")
        private val KEY_SMART_SUGGESTIONS = booleanPreferencesKey("smart_suggestions")
        private val KEY_ANONYMOUS_FEEDBACK = booleanPreferencesKey("anonymous_feedback_default")
        private val KEY_SALE_CONFIRMATION_SOUND = booleanPreferencesKey("sale_confirmation_sound")
        private val KEY_STOCK_ALERTS = booleanPreferencesKey("stock_alerts")
        private val KEY_SLOW_MOVER_ALERTS = booleanPreferencesKey("slow_mover_alerts")
    }

    // ── Session flows ────────────────────────────────────────────────

    /** Flow of the currently logged-in user ID (0 = no session). */
    val currentUserId: Flow<Long> = dataStore.data.map { prefs ->
        prefs[KEY_USER_ID] ?: 0L
    }

    /** Flow of the current business ID. */
    val currentBusinessId: Flow<Long> = dataStore.data.map { prefs ->
        prefs[KEY_BUSINESS_ID] ?: 0L
    }

    /** Flow of the current role ("owner" or "employee"). */
    val currentRole: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_ROLE]
    }

    /** Flow of the current user's display name. */
    val currentUserName: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_USER_NAME] ?: ""
    }

    /** Flow indicating whether a session exists. */
    val hasSession: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_USER_ID] != null && (prefs[KEY_ROLE] != null)
    }

    // ── Settings flows ───────────────────────────────────────────────

    /** Theme preference: "system" | "light" | "dark" */
    val themeMode: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_THEME] ?: "system"
    }

    /** Push notifications toggle (persisted, no real FCM in this phase). */
    val pushNotifications: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_PUSH_NOTIFICATIONS] ?: true
    }

    /** Sound toggle. */
    val soundEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_SOUND] ?: true
    }

    /** Promotions & offers toggle. */
    val promotionsEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_PROMOTIONS] ?: true
    }

    /** Chat notifications toggle. */
    val chatNotifications: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_CHAT_NOTIFICATIONS] ?: true
    }

    /** Smart suggestions toggle (rule-based tips on Budget screen). */
    val smartSuggestions: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_SMART_SUGGESTIONS] ?: true
    }

    /** Anonymous feedback default toggle. */
    val anonymousFeedbackDefault: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ANONYMOUS_FEEDBACK] ?: false
    }

    /** Sale confirmation sound toggle (Employee side). */
    val saleConfirmationSound: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_SALE_CONFIRMATION_SOUND] ?: true
    }

    /** Stock alerts toggle (Owner side). */
    val stockAlerts: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_STOCK_ALERTS] ?: true
    }

    /** Slow mover alerts toggle (Owner side). */
    val slowMoverAlerts: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_SLOW_MOVER_ALERTS] ?: true
    }

    // ── Session mutators ─────────────────────────────────────────────

    /** Save a session after login/signup. */
    suspend fun saveSession(userId: Long, businessId: Long, role: String, userName: String) {
        dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
            prefs[KEY_BUSINESS_ID] = businessId
            prefs[KEY_ROLE] = role
            prefs[KEY_USER_NAME] = userName
        }
    }

    /** Update just the business ID (e.g., after creating/joining a business). */
    suspend fun updateBusinessId(businessId: Long) {
        dataStore.edit { prefs ->
            prefs[KEY_BUSINESS_ID] = businessId
        }
    }

    /** Clear the session (logout). */
    suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_USER_ID)
            prefs.remove(KEY_BUSINESS_ID)
            prefs.remove(KEY_ROLE)
            prefs.remove(KEY_USER_NAME)
            // NOTE: Language preference is NOT cleared on logout.
            // The user's language choice persists across sessions.
        }
    }

    // ── Language ─────────────────────────────────────────────────────

    /** Flow of the selected language code. */
    val languageCode: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_LANGUAGE] ?: "en"
    }

    /** Save the user's language preference. */
    suspend fun saveLanguage(code: String) {
        dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = code
        }
    }

    /** Get the current language code as a one-shot value. */
    suspend fun getLanguageCode(): String {
        return dataStore.data.first()[KEY_LANGUAGE] ?: "en"
    }

    // ── Settings mutators ────────────────────────────────────────────

    suspend fun saveThemeMode(mode: String) {
        dataStore.edit { prefs -> prefs[KEY_THEME] = mode }
    }

    suspend fun savePushNotifications(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_PUSH_NOTIFICATIONS] = enabled }
    }

    suspend fun saveSoundEnabled(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_SOUND] = enabled }
    }

    suspend fun savePromotionsEnabled(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_PROMOTIONS] = enabled }
    }

    suspend fun saveChatNotifications(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_CHAT_NOTIFICATIONS] = enabled }
    }

    suspend fun saveSmartSuggestions(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_SMART_SUGGESTIONS] = enabled }
    }

    suspend fun saveAnonymousFeedbackDefault(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_ANONYMOUS_FEEDBACK] = enabled }
    }

    suspend fun saveSaleConfirmationSound(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_SALE_CONFIRMATION_SOUND] = enabled }
    }

    suspend fun saveStockAlerts(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_STOCK_ALERTS] = enabled }
    }

    suspend fun saveSlowMoverAlerts(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_SLOW_MOVER_ALERTS] = enabled }
    }

    // ── One-shot getters ─────────────────────────────────────────────

    /** Get the full User object for the current session. */
    suspend fun getCurrentUser(): User? {
        val uid = dataStore.data.first()[KEY_USER_ID] ?: 0L
        return if (uid > 0) userDao.getById(uid) else null
    }

    /** Get the current business ID as a one-shot value. */
    suspend fun getBusinessId(): Long {
        return dataStore.data.first()[KEY_BUSINESS_ID] ?: 0L
    }

    /** Get the current role as a one-shot value. */
    suspend fun getRole(): String? {
        return dataStore.data.first()[KEY_ROLE]
    }

    suspend fun getThemeMode(): String {
        return dataStore.data.first()[KEY_THEME] ?: "system"
    }

    suspend fun getSmartSuggestions(): Boolean {
        return dataStore.data.first()[KEY_SMART_SUGGESTIONS] ?: true
    }

    suspend fun getAnonymousFeedbackDefault(): Boolean {
        return dataStore.data.first()[KEY_ANONYMOUS_FEEDBACK] ?: false
    }

    suspend fun getPromotionsEnabled(): Boolean {
        return dataStore.data.first()[KEY_PROMOTIONS] ?: true
    }

    suspend fun getChatNotifications(): Boolean {
        return dataStore.data.first()[KEY_CHAT_NOTIFICATIONS] ?: true
    }

    suspend fun getSaleConfirmationSound(): Boolean {
        return dataStore.data.first()[KEY_SALE_CONFIRMATION_SOUND] ?: true
    }
}
