package com.duka.app.ui.screens.v4

/**
 * V4 Regional Roadmap Screen — static informational content.
 *
 * SCOPE-GUARD: This is a purely static Composable. No ViewModel, no database access,
 * no repository calls. It displays hardcoded content about Duka's expansion plans.
 *
 * MOCK STATUS: This is a roadmap/marketing screen. All content is static text.
 * No real backend, no real feature status tracking.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R

private data class RegionInfo(
    val country: String,
    val status: String, // "Live" | "Planned" | "Coming Soon"
    val description: String
)

private val REGIONS = listOf(
    RegionInfo("Rwanda", "Live", "Full access to Duka features — owner dashboard, marketplace, supply network, and government tools."),
    RegionInfo("Kenya", "Planned", "Expected Q2 2026. Adapting for KES currency and M-Pesa integration."),
    RegionInfo("Uganda", "Planned", "Expected Q3 2026. Adapting for UGX currency and MTN Mobile Money."),
    RegionInfo("Tanzania", "Coming Soon", "Exploring partnerships with local retail associations."),
    RegionInfo("Burundi", "Coming Soon", "Initial discussions with government trade ministry.")
)

@Composable
fun RegionalRoadmapScreen(
    onBack: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.roadmap_title), color = White) },
                navigationIcon = {
                    onBack?.let { back ->
                        IconButton(onClick = back) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Where Duka is headed",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                )
                Text(
                    "We're building a retail operating system for East Africa. Here's where we are and where we're going.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(REGIONS) { region ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                region.country,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                region.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                            )
                        }
                        // Status chip
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = when (region.status) {
                                "Live" -> Forest.copy(alpha = 0.1f)
                                "Planned" -> Amber.copy(alpha = 0.1f)
                                else -> Mist
                            }
                        ) {
                            Text(
                                region.status,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = when (region.status) {
                                        "Live" -> Forest
                                        "Planned" -> Amber
                                        else -> OnSurfaceVariant
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
