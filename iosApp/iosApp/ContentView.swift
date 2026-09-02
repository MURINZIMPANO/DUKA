import SwiftUI
import shared

struct ContentView: View {
    @State private var destination: String = "splash"
    @State private var isLoading: Bool = true

    var body: some View {
        Group {
            if isLoading {
                SplashView()
            } else {
                switch destination {
                case "login": AuthNavigationView()
                case "company_signup": AuthNavigationView()
                case "client_signup": AuthNavigationView()
                case "employee_login": AuthNavigationView()
                case "dashboard": OwnerTabView()
                case "employee_sell": EmployeeNavigationView()
                case "client_discover": ClientTabView()
                default: AuthNavigationView()
                }
            }
        }
        .onAppear {
            // In production, observe session state from shared ViewModel
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                isLoading = false
            }
        }
    }
}
