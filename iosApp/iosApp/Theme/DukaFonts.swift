import SwiftUI

extension Font {
    static func dukaTitle(_ size: CGFloat = 20) -> Font {
        .custom("SpaceGrotesk-Bold", size: size)
    }

    static func dukaBody(_ size: CGFloat = 14) -> Font {
        .custom("Inter-Regular", size: size)
    }

    static func dukaLabel(_ size: CGFloat = 14) -> Font {
        .custom("Inter-Medium", size: size)
    }

    static func dukaMono(_ size: CGFloat = 14) -> Font {
        .custom("IBMPlexMono-Regular", size: size)
    }
}
