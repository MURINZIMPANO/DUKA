package com.duka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R

@Composable
fun MoreScreen(
    onNavigateToChat: () -> Unit,
    onNavigateToPromo: () -> Unit,
    onNavigateToReportIssue: () -> Unit,
    // V3/V4 navigation
    onNavigateToCreditReadiness: () -> Unit = {},
    onNavigateToTrendingShops: () -> Unit = {},
    onNavigateToRequestRestock: () -> Unit = {},
    onNavigateToWholesalerMarketplace: () -> Unit = {},
    onNavigateToRegionalRoadmap: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    // V5 Employee Management
    onNavigateToEmployeeManagement: () -> Unit = {},
    // V6 Analytics
    onNavigateToAnalytics: () -> Unit = {},
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.more_title), color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // V1/V2 features
            MoreMenuItem(
                icon = Icons.Outlined.ChatBubbleOutline,
                title = stringResource(R.string.chat_title),
                subtitle = stringResource(R.string.more_chat_subtitle),
                iconTint = Forest,
                onClick = onNavigateToChat
            )

            MoreMenuItem(
                icon = Icons.Outlined.LocalOffer,
                title = stringResource(R.string.promo_title),
                subtitle = stringResource(R.string.more_promo_subtitle),
                iconTint = Forest,
                onClick = onNavigateToPromo
            )

            MoreMenuItem(
                icon = Icons.Outlined.Flag,
                title = stringResource(R.string.report_title),
                subtitle = stringResource(R.string.more_report_subtitle),
                iconTint = Clay,
                onClick = onNavigateToReportIssue
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.more_insights_section), style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant))
            Spacer(modifier = Modifier.height(4.dp))

            // V3 features
            MoreMenuItem(
                icon = Icons.Outlined.AccountBalance,
                title = stringResource(R.string.more_credit_title),
                subtitle = stringResource(R.string.more_credit_subtitle),
                iconTint = Forest,
                onClick = onNavigateToCreditReadiness
            )

            MoreMenuItem(
                icon = Icons.AutoMirrored.Outlined.TrendingUp,
                title = stringResource(R.string.more_trending_title),
                subtitle = stringResource(R.string.more_trending_subtitle),
                iconTint = Forest,
                onClick = onNavigateToTrendingShops
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.more_supply_section), style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant))
            Spacer(modifier = Modifier.height(4.dp))

            // V4 features
            MoreMenuItem(
                icon = Icons.Outlined.Restaurant,
                title = stringResource(R.string.more_restock_title),
                subtitle = stringResource(R.string.more_restock_subtitle),
                iconTint = Forest,
                onClick = onNavigateToRequestRestock
            )

            MoreMenuItem(
                icon = Icons.Outlined.Storefront,
                title = stringResource(R.string.more_wholesaler_title),
                subtitle = stringResource(R.string.more_wholesaler_subtitle),
                iconTint = Forest,
                onClick = onNavigateToWholesalerMarketplace
            )

            MoreMenuItem(
                icon = Icons.Outlined.Map,
                title = stringResource(R.string.more_roadmap_title),
                subtitle = stringResource(R.string.more_roadmap_subtitle),
                iconTint = OnSurfaceVariant,
                onClick = onNavigateToRegionalRoadmap
            )

            MoreMenuItem(
                icon = Icons.Outlined.Group,
                title = "Manage employees",
                subtitle = "View, add, and manage your team",
                iconTint = Forest,
                onClick = onNavigateToEmployeeManagement
            )

            MoreMenuItem(
                icon = Icons.AutoMirrored.Outlined.TrendingUp,
                title = "Analytics",
                subtitle = "Revenue, profit, tax estimates",
                iconTint = Forest,
                onClick = onNavigateToAnalytics
            )

            MoreMenuItem(
                icon = Icons.Outlined.Settings,
                title = stringResource(R.string.settings_title),
                subtitle = "Theme, language, notifications",
                iconTint = Forest,
                onClick = onNavigateToSettings
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Logout button
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = stringResource(R.string.logout),
                    modifier = Modifier.size(18.dp),
                    tint = Clay
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.logout), color = Clay)
            }
        }
    }
}

@Composable
private fun MoreMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconTint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = title, tint = iconTint)
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
            }
        }
    }
}
