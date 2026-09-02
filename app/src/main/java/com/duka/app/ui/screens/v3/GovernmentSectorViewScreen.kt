package com.duka.app.ui.screens.v3

/**
 * V3 Government Sector View Screen — sector overview with early-warning and complaints feed.
 *
 * SCOPE-GUARD: READS from V1/V2 IssueReport and V3 FeedbackReport tables.
 * Merges both into one chronological feed. No writes to any table.
 *
 * MOCK STATUS: All data is from locally-seeded IssueReport and FeedbackReport tables.
 * No real government backend or enforcement system.
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.FeedbackReportRepository
import com.duka.app.data.repository.IssueRepository
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R

@Composable
fun GovernmentSectorViewScreen(
    businessRepository: BusinessRepository,
    issueRepository: IssueRepository,
    feedbackReportRepository: FeedbackReportRepository,
    onBack: (() -> Unit)? = null
) {
    val issues by issueRepository.getReportsByBusiness(1L).collectAsState(initial = emptyList())
    val feedbacks by feedbackReportRepository.getAll().collectAsState(initial = emptyList())

    // Merge into chronological feed
    val combinedFeed = remember(issues, feedbacks) {
        val issueItems = issues.map { item ->
            FeedItem(
                source = "Business",
                category = item.category,
                message = item.message,
                timestamp = item.createdAt,
                status = item.status
            )
        }
        val feedbackItems = feedbacks.map { item ->
            FeedItem(
                source = "Client",
                category = item.category,
                message = item.message,
                timestamp = item.createdAt,
                status = item.status
            )
        }
        (issueItems + feedbackItems).sortedByDescending { it.timestamp }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gov_sector_title), color = White) },
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
            // Early warning card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Amber.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Early Warning",
                            style = MaterialTheme.typography.labelMedium.copy(color = Amber, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "No critical alerts at this time. All businesses operating normally.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Ink)
                        )
                    }
                }
            }

            // Support summary cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Needs Most Support", style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Gasabo", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Forest))
                            Text("3 open issues", style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Highest Hiring", style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Nyarugenge", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Forest))
                            Text("5 employees added", style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                        }
                    }
                }
            }

            // Sector growth comparison
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Sector Growth", style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Retail sector
                        Text("Retail", style = MaterialTheme.typography.bodySmall)
                        LinearProgressIndicator(
                            progress = 0.72f,
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = Forest,
                            trackColor = Mist,
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Services sector
                        Text("Services", style = MaterialTheme.typography.bodySmall)
                        LinearProgressIndicator(
                            progress = 0.45f,
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = Amber,
                            trackColor = Mist,
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Agriculture
                        Text("Agriculture", style = MaterialTheme.typography.bodySmall)
                        LinearProgressIndicator(
                            progress = 0.58f,
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = ForestLight,
                            trackColor = Mist,
                        )
                    }
                }
            }

            // Combined complaints/suggestions feed
            item {
                Text(
                    "Complaints & Suggestions",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                )
            }

            if (combinedFeed.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Text(
                            "No complaints or suggestions yet.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            items(combinedFeed) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                item.category,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            // Source chip
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (item.source == "Business") Forest.copy(alpha = 0.1f) else Amber.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    item.source,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (item.source == "Business") Forest else Amber
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.message, style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                java.time.Instant.ofEpochMilli(item.timestamp)
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .format(java.time.format.DateTimeFormatter.ofPattern("MMM d, HH:mm")),
                                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                            )
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Mist
                            ) {
                                Text(
                                    item.status,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class FeedItem(
    val source: String,
    val category: String,
    val message: String,
    val timestamp: Long,
    val status: String
)

// Need this import for the ForestLight color used in sector growth
private val ForestLight = com.duka.app.ui.theme.ForestLight
