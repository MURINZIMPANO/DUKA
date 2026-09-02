import SwiftUI

extension Color {
    static let dukaInk = Color(red: 0x1C/255, green: 0x23/255, blue: 0x21/255)           // #1C2321
    static let dukaCanvas = Color(red: 0xF3/255, green: 0xEF/255, blue: 0xE6/255)       // #F3EFE6
    static let dukaForest = Color(red: 0x1F/255, green: 0x4B/255, blue: 0x3F/255)       // #1F4B3F
    static let dukaForestLight = Color(red: 0x2C/255, green: 0x63/255, blue: 0x50/255)  // #2C6350
    static let dukaAmber = Color(red: 0xD9/255, green: 0xA4/255, blue: 0x41/255)        // #D9A441
    static let dukaClay = Color(red: 0x8B/255, green: 0x3A/255, blue: 0x2B/255)         // #8B3A2B
    static let dukaMist = Color(red: 0xE4/255, green: 0xDF/255, blue: 0xD3/255)         // #E4DFD3
    static let dukaWhite = Color.white

    // Semantic
    static let dukaBackground = Color.dukaCanvas
    static let dukaSurface = Color.dukaMist
    static let dukaOnBackground = Color.dukaInk
    static let dukaOnSurface = Color.dukaInk
    static let dukaOnSurfaceVariant = Color(red: 0x5A/255, green: 0x5A/255, blue: 0x5A/255)
}
