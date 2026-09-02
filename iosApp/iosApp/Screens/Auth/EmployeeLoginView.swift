import SwiftUI
import shared

struct EmployeeLoginView: View {
    @State private var code = ""
    @State private var isLoading = false
    @State private var error: String?

    var body: some View {
        VStack(spacing: 24) {
            Spacer()

            VStack(spacing: 8) {
                Image(systemName: "person.badge.key")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 60, height: 60)
                    .foregroundColor(Color.dukaForest)

                Text("Employee Login")
                    .font(.dukaTitle(24))
                    .foregroundColor(Color.dukaInk)

                Text("Enter your employee code")
                    .font(.dukaBody(14))
                    .foregroundColor(Color.dukaOnSurfaceVariant)
            }

            VStack(spacing: 12) {
                TextField("Employee Code", text: $code)
                    .font(.dukaMono(28))
                    .multilineTextAlignment(.center)
                    .keyboardType(.asciiCapable)
                    .autocorrectionDisabled(true)
                    .textInputAutocapitalization(.never)
                    .padding(16)
                    .background(Color.white)
                    .cornerRadius(12)
                    .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.dukaMist, lineWidth: 1))
            }
            .padding(.horizontal, 32)

            if let error = error {
                Text(error)
                    .font(.dukaBody(12))
                    .foregroundColor(Color.dukaClay)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
            }

            DukaPrimaryButton(label: "Log In") {
                login()
            }
            .disabled(isLoading || code.isEmpty)
            .padding(.horizontal, 32)

            if isLoading {
                ProgressView().tint(Color.dukaForest)
            }

            NavigationLink(destination: RoleSelectionView()) {
                Text("I'm a business owner")
                    .font(.dukaBody(14))
                    .foregroundColor(Color.dukaForest)
            }

            Spacer()
        }
        .background(Color.dukaCanvas)
        .navigationTitle("Employee Login")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func login() {
        guard !code.isEmpty else {
            error = "Please enter your employee code"
            return
        }
        isLoading = true
        error = nil
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            isLoading = false
        }
    }
}
