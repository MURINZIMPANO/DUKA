import SwiftUI
import shared

struct EmployeeNavigationView: View {
    var body: some View {
        TabView {
            NavigationStack {
                EmployeeSellView()
            }
            .tabItem {
                Label("Sell", systemImage: "square.grid.2x2")
            }

            NavigationStack {
                EmployeeChatView()
            }
            .tabItem {
                Label("Chat", systemImage: "bubble.left.and.bubble.right")
            }
        }
        .tint(Color.dukaForest)
    }
}

struct EmployeeSellView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    DukaCard {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Quick Sale")
                                .font(.dukaTitle(18))
                                .foregroundColor(Color.dukaInk)
                            Text("Scan or enter product to sell")
                                .font(.dukaBody(12))
                                .foregroundColor(Color.dukaOnSurfaceVariant)
                        }
                    }

                    DukaPrimaryButton(label: "Start Sale") { }

                    DukaSecondaryButton(label: "Voice Entry") { }
                }
                .padding(16)
            }
        }
        .navigationTitle("Sell")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                HStack(spacing: 12) {
                    NavigationLink(destination: EmployeeNotificationsView()) {
                        Image(systemName: "bell")
                            .foregroundColor(Color.dukaForest)
                    }
                    NavigationLink(destination: SettingsView(role: "employee")) {
                        Image(systemName: "gearshape")
                            .foregroundColor(Color.dukaForest)
                    }
                }
            }
        }
    }
}

struct EmployeeChatView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            VStack {
                Spacer()
                Text("Chat with your team")
                    .font(.dukaBody(14))
                    .foregroundColor(Color.dukaOnSurfaceVariant)
                Spacer()
            }
        }
        .navigationTitle("Chat")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct EmployeeNotificationsView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 12) {
                    Text("No notifications yet")
                        .font(.dukaBody(14))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                        .padding(.top, 32)
                }
                .padding(16)
            }
        }
        .navigationTitle("Notifications")
        .navigationBarTitleDisplayMode(.inline)
    }
}
