import SwiftUI
import shared

struct OwnerSignupView: View {
    @State private var ownerName = ""
    @State private var phoneOrEmail = ""
    @State private var password = ""
    @State private var businessName = ""
    @State private var businessType = "Supermarket"
    @State private var employeeCount = 1
    @State private var language = "English"
    @State private var district = "Gasabo"
    @State private var isLoading = false
    @State private var error: String?

    let businessTypes = ["Supermarket", "Grocery", "Pharmacy", "Electronics", "Clothing", "Restaurant", "Other"]
    let languages = ["English", "Kinyarwanda", "French"]
    let districts = ["Gasabo", "Kicukiro", "Nyarugenge", "Musanze", "Rubavu", "Huye"]

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                Text("Create Your Shop")
                    .font(.dukaTitle(22))
                    .foregroundColor(Color.dukaInk)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 8)

                FormSection(title: "Your Details") {
                    DukaTextField(icon: "person", placeholder: "Full Name", text: $ownerName)
                    DukaTextField(icon: "envelope", placeholder: "Phone or Email", text: $phoneOrEmail)
                    DukaTextField(icon: "lock", placeholder: "Password", text: $password, isSecure: true)
                }

                FormSection(title: "Business Details") {
                    DukaTextField(icon: "building.2", placeholder: "Business Name", text: $businessName)

                    HStack {
                        Image(systemName: "tag")
                            .foregroundColor(Color.dukaForest)
                        Picker("Type", selection: $businessType) {
                            ForEach(businessTypes, id: \.self) { Text($0) }
                        }
                        .pickerStyle(.menu)
                    }
                    .padding(12)
                    .background(Color.white)
                    .cornerRadius(10)

                    HStack {
                        Image(systemName: "person.3")
                            .foregroundColor(Color.dukaForest)
                        Stepper("Employees: \(employeeCount)", value: $employeeCount, in: 1...100)
                    }
                    .padding(12)
                    .background(Color.white)
                    .cornerRadius(10)

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
                    .cornerRadius(10)

                    HStack {
                        Image(systemName: "mappin.circle")
                            .foregroundColor(Color.dukaForest)
                        Picker("District", selection: $district) {
                            ForEach(districts, id: \.self) { Text($0) }
                        }
                        .pickerStyle(.menu)
                    }
                    .padding(12)
                    .background(Color.white)
                    .cornerRadius(10)
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
        guard !ownerName.isEmpty, !phoneOrEmail.isEmpty, !password.isEmpty, !businessName.isEmpty else {
            error = "Please fill in all fields"
            return
        }
        isLoading = true
        error = nil
        // In production, call shared SessionViewModel
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            isLoading = false
        }
    }
}

// MARK: - Form Helpers
struct FormSection<Content: View>: View {
    let title: String
    @ViewBuilder let content: () -> Content

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.dukaLabel(14))
                .foregroundColor(Color.dukaOnSurfaceVariant)
                .padding(.leading, 4)

            VStack(spacing: 1) {
                content
            }
            .background(Color.white)
            .cornerRadius(12)
            .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.dukaMist, lineWidth: 1))
        }
    }
}

struct DukaTextField: View {
    let icon: String
    let placeholder: String
    @Binding var text: String
    var isSecure: Bool = false

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .foregroundColor(Color.dukaForest)
                .frame(width: 20)

            if isSecure {
                SecureField(placeholder, text: $text)
                    .font(.dukaBody(14))
            } else {
                TextField(placeholder, text: $text)
                    .font(.dukaBody(14))
            }
        }
        .padding(12)
        .background(Color.white)
    }
}
