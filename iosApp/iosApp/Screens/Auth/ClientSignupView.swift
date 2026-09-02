import SwiftUI
import shared

struct ClientSignupView: View {
    @State private var fullName = ""
    @State private var phoneOrEmail = ""
    @State private var password = ""
    @State private var language = "English"
    @State private var isLoading = false
    @State private var error: String?

    let languages = ["English", "Kinyarwanda", "French"]

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                Text("Create Your Account")
                    .font(.dukaTitle(22))
                    .foregroundColor(Color.dukaInk)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 8)

                FormSection(title: "Your Details") {
                    DukaTextField(icon: "person", placeholder: "Full Name", text: $fullName)
                    DukaTextField(icon: "envelope", placeholder: "Phone or Email", text: $phoneOrEmail)
                    DukaTextField(icon: "lock", placeholder: "Password", text: $password, isSecure: true)

                    HStack {
                        Image(systemName: "globe")
                            .foregroundColor(Color.dukaForest)
                        Picker("Language", selection: $language) {
                            ForEach(languages, id: \.self) { Text($0) }
                        }
                        .pickerStyle(.menu)
                    }
                    .padding(12)
                    .background(Color.white)
                }

                if let error = error {
                    Text(error)
                        .font(.dukaBody(12))
                        .foregroundColor(Color.dukaClay)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }

                DukaPrimaryButton(label: "Create Account") {
                    signup()
                }
                .disabled(isLoading)

                if isLoading {
                    ProgressView().tint(Color.dukaForest)
                }
            }
            .padding(16)
        }
        .background(Color.dukaCanvas)
        .navigationTitle("Sign Up")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func signup() {
        guard !fullName.isEmpty, !phoneOrEmail.isEmpty, !password.isEmpty else {
            error = "Please fill in all fields"
            return
        }
        isLoading = true
        error = nil
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            isLoading = false
        }
    }
}
