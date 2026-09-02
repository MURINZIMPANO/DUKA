import SwiftUI
import shared

@main
struct DukaApp: App {
    init() {
        KoinInitKt.doInitKoin()
        // Seed demo data asynchronously — does not block app launch
        KoinInitKt.seedAppData()

        // If running in a simulator context with no saved session, always start at login
        // This helps online simulator tools show the app from a clean state
        if CommandLine.arguments.contains("--reset-session") ||
           ProcessInfo.processInfo.environment["SIMULATOR_CLEAN_LAUNCH"] != nil {
            UserDefaults.standard.removeObject(forKey: "duka_active_session")
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
