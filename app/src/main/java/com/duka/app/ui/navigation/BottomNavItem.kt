package com.duka.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom navigation items with Outlined (default) / Filled (active) icon swap.
 * This filled/outlined swap is a cheap, effective piece of polish per the design spec.
 */
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Dashboard : BottomNavItem(
        NavRoutes.DASHBOARD, "Dashboard",
        Icons.Outlined.Dashboard, Icons.Filled.Dashboard
    )
    data object Products : BottomNavItem(
        NavRoutes.PRODUCTS, "Products",
        Icons.Outlined.Inventory2, Icons.Filled.Inventory2
    )
    data object Tax : BottomNavItem(
        NavRoutes.TAX_EBM, "Tax",
        Icons.AutoMirrored.Outlined.ReceiptLong, Icons.AutoMirrored.Filled.ReceiptLong
    )
    data object Chat : BottomNavItem(
        NavRoutes.CHAT, "Chat",
        Icons.Outlined.ChatBubbleOutline, Icons.Filled.Chat
    )
    data object More : BottomNavItem(
        NavRoutes.MORE, "More",
        Icons.Outlined.MoreHoriz, Icons.Filled.MoreHoriz
    )
}
