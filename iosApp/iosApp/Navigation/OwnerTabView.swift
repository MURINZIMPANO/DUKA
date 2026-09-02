import SwiftUI
import shared

struct OwnerTabView: View {
    var body: some View {
        TabView {
            NavigationStack {
                OwnerDashboardView()
            }
            .tabItem {
                Label("Dashboard", systemImage: "square.grid.2x2")
            }

            NavigationStack {
                OwnerProductsView()
            }
            .tabItem {
                Label("Products", systemImage: "shippingbox")
            }

            NavigationStack {
                TaxEbmView()
            }
            .tabItem {
                Label("Tax", systemImage: "receipt")
            }

            NavigationStack {
                OwnerChatView()
            }
            .tabItem {
                Label("Chat", systemImage: "bubble.left.and.bubble.right")
            }

            NavigationStack {
                OwnerMoreView()
            }
            .tabItem {
                Label("More", systemImage: "ellipsis")
            }
        }
        .tint(Color.dukaForest)
    }
}

// MARK: - Dashboard
struct OwnerDashboardView: View {
    @State private var todayRevenue: String = "0"
    @State private var totalProducts: String = "0"
    @State private var activeEmployees: String = "0"
    @State private var lowStockCount: String = "0"

    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()

            ScrollView {
                VStack(spacing: 16) {
                    // Stats row
                    HStack(spacing: 12) {
                        DukaStatCard(label: "Today's Sales", value: "RWF \(todayRevenue)", icon: "banknote")
                        DukaStatCard(label: "Products", value: totalProducts, icon: "shippingbox")
                    }

                    HStack(spacing: 12) {
                        DukaStatCard(label: "Employees", value: activeEmployees, icon: "person.3")
                        DukaStatCard(label: "Low Stock", value: lowStockCount, icon: "exclamationmark.triangle")
                    }

                    // Quick actions
                    DukaSectionHeader(title: "Quick Actions")
                    VStack(spacing: 12) {
                        QuickActionRow(icon: "plus.circle.fill", title: "Add Product", subtitle: "Add a new product to inventory")
                        QuickActionRow(icon: "mic.fill", title: "Voice Add", subtitle: "Add product by speaking")
                        QuickActionRow(icon: "chart.pie", title: "Analytics", subtitle: "View business insights")
                        QuickActionRow(icon: "exclamationmark.triangle", title: "Alerts", subtitle: "Check low stock & slow movers")
                    }
                    .padding(.bottom, 16)
                }
                .padding(.horizontal, 16)
            }
        }
        .navigationTitle("Dashboard")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                HStack(spacing: 12) {
                    NavigationLink(destination: OwnerNotificationsView()) {
                        Image(systemName: "bell")
                            .foregroundColor(Color.dukaForest)
                    }
                    NavigationLink(destination: SettingsView(role: "owner")) {
                        Image(systemName: "gearshape")
                            .foregroundColor(Color.dukaForest)
                    }
                }
            }
        }
    }
}

struct QuickActionRow: View {
    let icon: String
    let title: String
    let subtitle: String

    var body: some View {
        DukaCard {
            HStack(spacing: 14) {
                Image(systemName: icon)
                    .font(.title2)
                    .foregroundColor(Color.dukaForest)
                    .frame(width: 40, height: 40)

                VStack(alignment: .leading, spacing: 2) {
                    Text(title)
                        .font(.dukaLabel(14))
                        .foregroundColor(Color.dukaInk)
                    Text(subtitle)
                        .font(.dukaBody(11))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }

                Spacer()

                Image(systemName: "chevron.right")
                    .font(.caption)
                    .foregroundColor(Color.dukaOnSurfaceVariant)
            }
        }
    }
}

// MARK: - Products
struct OwnerProductsView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    DukaSectionHeader(title: "Inventory")
                    Text("Product list will appear here")
                        .font(.dukaBody(14))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
                .padding(16)
            }
        }
        .navigationTitle("Products")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                NavigationLink(destination: AddProductView()) {
                    Image(systemName: "plus.circle.fill")
                        .foregroundColor(Color.dukaAmber)
                }
            }
        }
    }
}

struct AddProductView: View {
    @State private var name = ""
    @State private var price = ""
    @State private var stock = ""
    @State private var category = "Food"

    let categories = ["Food", "Beverages", "Personal Care", "Electronics", "Clothing", "Other"]

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                FormSection(title: "Product Details") {
                    DukaTextField(icon: "tag", placeholder: "Product Name", text: $name)
                    DukaTextField(icon: "banknote", placeholder: "Price (RWF)", text: $price)
                    DukaTextField(icon: "number", placeholder: "Stock Quantity", text: $stock)

                    HStack {
                        Image(systemName: "folder")
                            .foregroundColor(Color.dukaForest)
                        Picker("Category", selection: $category) {
                            ForEach(categories, id: \.self) { Text($0) }
                        }
                        .pickerStyle(.menu)
                    }
                    .padding(12)
                    .background(Color.white)
                }

                DukaPrimaryButton(label: "Add Product") { }
            }
            .padding(16)
        }
        .background(Color.dukaCanvas)
        .navigationTitle("Add Product")
        .navigationBarTitleDisplayMode(.inline)
    }
}

// MARK: - Tax/EBM
struct TaxEbmView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    DukaCard {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Tax Status")
                                .font(.dukaTitle(16))
                                .foregroundColor(Color.dukaInk)
                            DukaChip(label: "Exempt", style: .forest)
                            Text("Annual turnover below 2M RWF — no income tax required")
                                .font(.dukaBody(12))
                                .foregroundColor(Color.dukaOnSurfaceVariant)
                        }
                    }

                    DukaSectionHeader(title: "Recent Receipts")
                    Text("EBM receipts will appear here")
                        .font(.dukaBody(14))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
                .padding(16)
            }
        }
        .navigationTitle("Tax & EBM")
        .navigationBarTitleDisplayMode(.inline)
    }
}

// MARK: - Chat
struct OwnerChatView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            VStack {
                Spacer()
                Text("Chat feature coming soon")
                    .font(.dukaBody(14))
                    .foregroundColor(Color.dukaOnSurfaceVariant)
                Spacer()
            }
        }
        .navigationTitle("Chat")
        .navigationBarTitleDisplayMode(.inline)
    }
}

// MARK: - More
struct OwnerMoreView: View {
    var body: some View {
        List {
            Section("Insights") {
                NavigationLink("Analytics Hub", destination: Text("Analytics"))
                NavigationLink("Credit Readiness", destination: Text("Credit"))
                NavigationLink("Trending Shops", destination: Text("Trending"))
            }
            Section("Supply Network") {
                NavigationLink("Request Restock", destination: Text("Restock"))
                NavigationLink("Wholesaler Marketplace", destination: Text("Marketplace"))
                NavigationLink("Regional Roadmap", destination: Text("Roadmap"))
            }
            Section("Support") {
                NavigationLink("Promo", destination: Text("Promo"))
                NavigationLink("Report Issue", destination: Text("Issue"))
            }
            Section {
                NavigationLink(destination: EmployeeManagementView()) {
                    Label("Employee Management", systemImage: "person.badge.key")
                }
                NavigationLink(destination: SettingsView(role: "owner")) {
                    Label("Settings", systemImage: "gearshape")
                }
            }
        }
        .navigationTitle("More")
        .tint(Color.dukaForest)
    }
}

// MARK: - Notifications
struct OwnerNotificationsView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 12) {
                    Text("Notifications will appear here")
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

// MARK: - Employee Management
struct EmployeeManagementView: View {
    var body: some View {
        ZStack {
            Color.dukaCanvas.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    DukaSectionHeader(title: "Team Members")
                    Text("Employee list will appear here")
                        .font(.dukaBody(14))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                }
                .padding(16)
            }
        }
        .navigationTitle("Team")
        .navigationBarTitleDisplayMode(.inline)
    }
}
