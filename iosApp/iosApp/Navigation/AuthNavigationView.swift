import SwiftUI
import shared

struct AuthNavigationView: View {
    @State private var showSplash = true

    var body: some View {
        NavigationStack {
            if showSplash {
                SplashView()
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                            showSplash = false
                        }
                    }
            } else {
                RoleSelectionView()
            }
        }
    }
}

// MARK: - Splash
struct SplashView: View {
    @State private var opacity: Double = 0

    var body: some View {
        ZStack {
            Color.dukaForest
                .ignoresSafeArea()

            VStack(spacing: 16) {
                Image(systemName: "storefront")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 80, height: 80)
                    .foregroundColor(.white)

                Text("Duka")
                    .font(.dukaTitle(32))
                    .foregroundColor(.white)

                Text("Retail Operating System")
                    .font(.dukaBody(14))
                    .foregroundColor(.white.opacity(0.8))
            }
            .opacity(opacity)
        }
        .onAppear {
            withAnimation(.easeIn(duration: 0.8)) { opacity = 1 }
        }
    }
}

// MARK: - Role Selection
struct RoleSelectionView: View {
    var body: some View {
        VStack(spacing: 32) {
            Spacer()

            Text("Welcome to Duka")
                .font(.dukaTitle(24))
                .foregroundColor(Color.dukaInk)

            Text("How would you like to use Duka?")
                .font(.dukaBody(16))
                .foregroundColor(Color.dukaOnSurfaceVariant)

            VStack(spacing: 16) {
                NavigationLink(destination: OwnerSignupView()) {
                    RoleCard(
                        icon: "storefront",
                        title: "Business Owner",
                        description: "Manage your shop"
                    )
                }

                NavigationLink(destination: EmployeeLoginView()) {
                    RoleCard(
                        icon: "person.badge.key",
                        title: "Employee",
                        description: "Access your account"
                    )
                }

                NavigationLink(destination: ClientSignupView()) {
                    RoleCard(
                        icon: "cart",
                        title: "Shopper",
                        description: "Discover and shop"
                    )
                }
            }
            .padding(.horizontal, 24)

            // Demo accounts hint
            DemoAccountsHint()

            Spacer()
        }
        .background(Color.dukaCanvas)
    }
}

struct DemoAccountsHint: View {
    @State private var showDemo = false

    var body: some View {
        VStack(spacing: 8) {
            Button(action: { showDemo.toggle() }) {
                Text(showDemo ? "Hide demo accounts" : "Demo accounts — remove before publishing")
                    .font(.dukaBody(12))
                    .foregroundColor(Color.dukaOnSurfaceVariant)
            }

            if showDemo {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Owner:     0788000001  /  demo1234")
                        .font(.system(.caption, design: .monospaced))
                        .foregroundColor(Color.dukaForest)
                    Text("Employee:  code JB7X2K")
                        .font(.system(.caption, design: .monospaced))
                        .foregroundColor(Color.dukaForest)
                    Text("Client:    0788000002  /  client1234")
                        .font(.system(.caption, design: .monospaced))
                        .foregroundColor(Color.dukaForest)
                }
                .padding(12)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color.dukaForest.opacity(0.05))
                .cornerRadius(12)
                .padding(.horizontal, 24)
            }
        }
    }
}

struct RoleCard: View {
    let icon: String
    let title: String
    let description: String

    var body: some View {
        HStack(spacing: 16) {
            Image(systemName: icon)
                .resizable()
                .scaledToFit()
                .frame(width: 32, height: 32)
                .foregroundColor(Color.dukaForest)

            VStack(alignment: .leading, spacing: 4) {
                Text(title)
                    .font(.dukaLabel(16))
                    .foregroundColor(Color.dukaInk)
                Text(description)
                    .font(.dukaBody(12))
                    .foregroundColor(Color.dukaOnSurfaceVariant)
            }

            Spacer()

            Image(systemName: "chevron.right")
                .foregroundColor(Color.dukaOnSurfaceVariant)
        }
        .padding(16)
        .background(Color.white)
        .cornerRadius(12)
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.dukaMist, lineWidth: 1))
    }
}
