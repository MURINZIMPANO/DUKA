package com.duka.shared.notifications

import com.duka.shared.data.session.SessionManager

/**
 * Helper to check notification preferences before posting.
 * Platform-agnostic — session manager handles the actual storage.
 */
class NotificationPreferences(
    private val sessionManager: SessionManager
) {
    suspend fun shouldNotify(type: String): Boolean {
        return when (type) {
            "chat_message" -> sessionManager.getChatNotifications()
            "promo_live" -> sessionManager.getPromotionsEnabled()
            else -> sessionManager.getPushNotifications()
        }
    }

    suspend fun shouldPlaySound(): Boolean {
        return sessionManager.getSoundEnabled()
    }
}
