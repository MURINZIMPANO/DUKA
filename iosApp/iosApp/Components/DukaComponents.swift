import SwiftUI

// MARK: - Primary Button
struct DukaPrimaryButton: View {
    let label: String
    let action: () -> Void
    @State private var isPressed = false

    var body: some View {
        Button(action: action) {
            Text(label)
                .font(.dukaLabel(16))
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 14)
                .background(Color.dukaForest)
                .cornerRadius(10)
                .scaleEffect(isPressed ? 0.97 : 1.0)
                .animation(.spring(response: 0.2, dampingFraction: 0.6), value: isPressed)
        }
        .buttonStyle(PlainButtonStyle())
        .simultaneousGesture(
            DragGesture(minimumDistance: 0)
                .onChanged { _ in isPressed = true }
                .onEnded { _ in isPressed = false }
        )
    }
}

// MARK: - Secondary Button
struct DukaSecondaryButton: View {
    let label: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(label)
                .font(.dukaLabel(14))
                .foregroundColor(Color.dukaForest)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 12)
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(Color.dukaForest, lineWidth: 1.5)
                )
        }
        .buttonStyle(PlainButtonStyle())
    }
}

// MARK: - Chip
enum ChipStyle {
    case forest, amber, clay, mist

    var textColor: Color {
        switch self {
        case .forest: return Color.dukaForest
        case .amber: return Color.dukaAmber
        case .clay: return Color.dukaClay
        case .mist: return Color.dukaMist
        }
    }

    var borderColor: Color {
        switch self {
        case .forest: return Color.dukaForest
        case .amber: return Color.dukaAmber
        case .clay: return Color.dukaClay
        case .mist: return Color.dukaMist
        }
    }
}

struct DukaChip: View {
    let label: String
    let style: ChipStyle

    var body: some View {
        Text(label)
            .font(.dukaBody(12))
            .foregroundColor(style.textColor)
            .padding(.horizontal, 10)
            .padding(.vertical, 4)
            .overlay(Capsule().stroke(style.borderColor, lineWidth: 1))
    }
}

// MARK: - Card Container
struct DukaCard<Content: View>: View {
    let content: () -> Content

    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }

    var body: some View {
        content()
            .padding(16)
            .background(Color.white)
            .cornerRadius(12)
            .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.dukaMist, lineWidth: 1))
            .shadow(color: .black.opacity(0.04), radius: 4, x: 0, y: 2)
    }
}

// MARK: - Section Header
struct DukaSectionHeader: View {
    let title: String
    var body: some View {
        Text(title)
            .font(.dukaTitle(16))
            .foregroundColor(Color.dukaInk)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}

// MARK: - Stat Card
struct DukaStatCard: View {
    let label: String
    let value: String
    let icon: String

    var body: some View {
        DukaCard {
            VStack(alignment: .leading, spacing: 8) {
                HStack {
                    Image(systemName: icon)
                        .foregroundColor(Color.dukaForest)
                    Text(label)
                        .font(.dukaBody(12))
                        .foregroundColor(Color.dukaOnSurfaceVariant)
                    Spacer()
                }
                Text(value)
                    .font(.dukaMono(20))
                    .foregroundColor(Color.dukaInk)
            }
        }
    }
}

// MARK: - Loading View
struct DukaLoadingView: View {
    var body: some View {
        ProgressView()
            .tint(Color.dukaForest)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(Color.dukaCanvas)
    }
}
