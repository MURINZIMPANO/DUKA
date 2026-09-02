import UserNotifications

/**
 * iOS notification service — thin wrapper around UNUserNotificationCenter.
 * No business logic — called from shared ViewModel or platform-specific code.
 */
class NotificationService {
    static func requestPermission() async -> Bool {
        do {
            return try await UNUserNotificationCenter.current()
                .requestAuthorization(options: [.alert, .sound, .badge])
        } catch {
            return false
        }
    }

    static func postNotification(title: String, body: String, identifier: String) {
        let content = UNMutableNotificationContent()
        content.title = title
        content.body = body
        content.sound = .default

        let request = UNNotificationRequest(
            identifier: identifier,
            content: content,
            trigger: nil
        )
        UNUserNotificationCenter.current().add(request)
    }

    static func clearNotification(identifier: String) {
        UNUserNotificationCenter.current()
            .removePendingNotificationRequests(withIdentifiers: [identifier])
    }

    static func clearAllNotifications() {
        UNUserNotificationCenter.current()
            .removeAllPendingNotificationRequests()
    }
}
