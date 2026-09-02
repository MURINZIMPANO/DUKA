import SwiftUI
import shared

struct SettingsView: View {
    let role: String
    @State private var showLogoutConfirmation = false

    var body: some View {
        Form {
            Section("Account") {
                HStack {
                    Image(systemName: "person.circle")
                        .foregroundColor(Color.dukaForest)
                    Text("Account Info")
                }
            }

            Section("Appearance") {
                HStack {
                    Image(systemName: "paintbrush")
                        .foregroundColor(Color.dukaForest)
                    Text("Theme")
                    Spacer()
                    Text("System")
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
                HStack {
                    Image(systemName: "globe")
                        .foregroundColor(Color.dukaForest)
                    Text("Language")
                    Spacer()
                    Text("English")
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
            }

            if role == "owner" || role == "client" {
                Section("Notifications") {
                    Toggle(isOn: .constant(true)) {
                        Label("Push Notifications", systemImage: "bell")
                    }
                    Toggle(isOn: .constant(true)) {
                        Label("Sound", systemImage: "speaker.wave.2")
                    }
                    if role == "owner" {
                        Toggle(isOn: .constant(true)) {
                            Label("Stock Alerts", systemImage: "exclamationmark.triangle")
                        }
                    }
                }
            }

            if role == "owner" {
                Section("Employee Settings") {
                    Toggle(isOn: .constant(true)) {
                        Label("Sale Confirmation Sound", systemImage: "checkmark.circle")
                    }
                }
            }

            Section("About") {
                HStack {
                    Text("Version")
                    Spacer()
                    Text("1.0.0")
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
            }

            Section {
                Button(role: .destructive) {
                    showLogoutConfirmation = true
                } label: {
                    Label("Log out", systemImage: "rectangle.portrait.and.arrow.right")
                }
            }
        }
        .navigationTitle("Settings")
        .tint(Color.dukaForest)
        .confirmationDialog(
            "Log out of Duka?",
            isPresented: $showLogoutConfirmation,
            titleVisibility: .visible
        ) {
            Button("Log out", role: .destructive) { logout() }
            Button("Cancel", role: .cancel) {}
        }
    }

    private func logout() {
        // In production, call shared SessionViewModel.logout()
    }
}
