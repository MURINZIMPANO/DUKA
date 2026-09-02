import SwiftUI
import shared

struct ClientTabView: View {
    var body: some View {
        TabView {
            NavigationStack {
                ClientDiscoverView()
            }
            .tabItem {
                Label("Discover", systemImage: "storefront")
            }

            NavigationStack {
                ClientBudgetView()
            }
            .tabItem {
                Label("Spending", systemImage: "wallet.pass")
            }

            NavigationStack {
                ClientFeedbackView()
            }
            .tabItem {
                Label("Feedback", systemImage: "bubble.left.and.exclamation.mark.bubble.right")
            }
        }
        .tint(Color.dukaForest)
    }
}

// MARK: - Discover
struct ClientDiscoverView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    DukaSectionHeader(title: "Nearby Shops")
                    Text("Shop listings will appear here")
                        .font(.dukaBody(14))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
                .padding(16)
            }
        }
        .navigationTitle("Discover")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                HStack(spacing: 12) {
                    NavigationLink(destination: ClientNotificationsView()) {
                        Image(systemName: "bell")
                            .foregroundColor(Color.dukaForest)
                    }
                    NavigationLink(destination: SettingsView(role: "client")) {
                        Image(systemName: "gearshape")
                            .foregroundColor(Color.dukaForest)
                    }
                }
            }
        }
    }
}

// MARK: - Budget
struct ClientBudgetView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    DukaCard {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Weekly Budget")
                                .font(.dukaTitle(16))
                                .foregroundColor(Color.dukaInk)
                            Text("RWF 0 / RWF 50,000")
                                .font(.dukaMono(20))
                                .foregroundColor(Color.dukaForest)
                        }
                    }

                    DukaSectionHeader(title: "Recent Spending")
                    Text("Spending history will appear here")
                        .font(.dukaBody(14))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
                .padding(16)
            }
        }
        .navigationTitle("Spending")
        .navigationBarTitleDisplayMode(.inline)
    }
}

// MARK: - Feedback
struct ClientFeedbackView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    DukaSectionHeader(title: "Your Feedback")
                    Text("Submit and view your feedback")
                        .font(.dukaBody(14))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
                .padding(16)
            }
        }
        .navigationTitle("Feedback")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct ClientNotificationsView: View {
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
