package com.duka.notifications

import com.duka.app.data.session.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper to check notification preferences before posting.
 * Called before every NotificationManagerCompat.notify() call.
 */
@Singleton
class NotificationPreferences @Inject constructor(
    private val sessionManager: SessionManager
) {
    /**
     * Check if notifications should be posted for a given type.
     * Checks the DataStore preferences before each notification post.
     */
    suspend fun shouldNotify(type: String): Boolean {
        return when (type) {
            "chat_message" -> sessionManager.getChatNotifications()
            "promo_live" -> sessionManager.getPromotionsEnabled()
            // All other types respect the global push notification toggle
            else -> {
                var enabled = true
                sessionManager.pushNotifications.collect { enabled = it }
                enabled
            }
        }
    }

    /**
     * Check if sound should be included in notification.
     */
    suspend fun shouldPlaySound(): Boolean {
        var enabled = true
        sessionManager.soundEnabled.collect { enabled = it }
        return enabled
    }
}
