package com.duka.app.ui.screens.v3

/**
 * V3 Government Tax Engine Screen — admin-only aggregate tax data view.
 *
 * SCOPE-GUARD: READS from V1/V2 Business, Sale tables via existing repositories.
 * All queries are aggregate (SUM, COUNT, GROUP BY) run off main thread.
 * No new write paths to V1/V2 tables.
 *
 * MOCK STATUS: District/sector filters are local. No real geo-fencing or government backend.
 * Decision queue status updates are local-only. No real enforcement action.
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.PurchaseRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.app.domain.TaxCalculator
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GovernmentTaxEngineScreen(
    businessRepository: BusinessRepository,
    saleRepository: SaleRepository,
    purchaseRepository: PurchaseRepository,
    onBack: (() -> Unit)? = null
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    var selectedPeriod by remember { mutableStateOf("This quarter") }
    val periods = listOf("This quarter", "This year", "All time")
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    // Compute aggregate tax data from all businesses
    var totalTurnover by remember { mutableStateOf(0.0) }
    var totalSalesCount by remember { mutableStateOf(0) }
    var businessCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // For demo, use the single business's data as the "national" aggregate
        val biz = businessRepository.getActiveBusinessOnce()
        if (biz != null) {
            businessCount = 1
            totalTurnover = saleRepository.getTotalAllTime(biz.id) ?: 0.0
            totalSalesCount = saleRepository.getSalesCountInRange(
                biz.id, 0L, System.currentTimeMillis()
            ).let { flow ->
                var count = 0
                flow.collect { count = it }
                count
            }
        }
    }

    val quarterlyTurnover = when (selectedPeriod) {
        "This quarter" -> totalTurnover / 4.0
        "This year" -> totalTurnover
        else -> totalTurnover
    }
    val taxResult = TaxCalculator.compute(quarterlyTurnover)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gov_tax_title), color = White) },
                navigationIcon = {
                    onBack?.let { back ->
                        androidx.compose.material3.IconButton(onClick = back) {
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
            // Period filter
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    periods.forEach { period ->
                        FilterChip(
                            selected = selectedPeriod == period,
                            onClick = { selectedPeriod = period },
                            label = { Text(period) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Forest,
                                selectedLabelColor = White,
                                containerColor = White,
                                labelColor = OnSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Tax tier breakdown card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Income Tier Breakdown", style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            taxResult.tier.label,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = when (taxResult.tier) {
                                    TaxCalculator.TaxTier.EXEMPT -> Forest
                                    TaxCalculator.TaxTier.FLAT -> Forest
                                    TaxCalculator.TaxTier.LUMP_SUM -> Amber
                                    TaxCalculator.TaxTier.FULL_VAT -> Clay
                                }
                            )
                        )
                        Text(taxResult.tier.description, style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                    }
                }
            }

            // Aggregate metrics
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
                            Text("Total Turnover", style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "RWF ${currencyFormat.format(taxResult.annualTurnover)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Forest)
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Est. Annual Tax", style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "RWF ${currencyFormat.format(taxResult.estimatedTax)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Ink)
                            )
                        }
                    }
                }
            }

            // Platform adoption
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Platform Adoption", style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Registered businesses", style = MaterialTheme.typography.bodyMedium)
                            Text("$businessCount", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total transactions", style = MaterialTheme.typography.bodyMedium)
                            Text("$totalSalesCount", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }

            // Foreign vs local comparison
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Income Source Comparison", style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Local income bar
                        Text("Local Sales", style = MaterialTheme.typography.bodySmall)
                        LinearProgressIndicator(
                            progress = 0.75f,
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = Forest,
                            trackColor = Mist,
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Client/external bar
                        Text("External Purchases", style = MaterialTheme.typography.bodySmall)
                        LinearProgressIndicator(
                            progress = 0.25f,
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = Amber,
                            trackColor = Mist,
                        )
                    }
                }
            }

            // Decision queue (mock)
            item {
                Text(
                    "Decision Queue",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Ink)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tax compliance review", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                            Text("Kigali Fresh Mart — Pending", style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { /* Dismiss */ }) {
                                Text("Dismiss", style = MaterialTheme.typography.labelSmall)
                            }
                            OutlinedButton(onClick = { /* Open case */ }) {
                                Text("Open", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Note: Decision queue is for demo purposes only. No real enforcement action is taken.",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                )
            }
        }
    }
}
