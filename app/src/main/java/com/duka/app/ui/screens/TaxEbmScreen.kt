package com.duka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.EbmRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.app.data.repository.TaxRepository
import com.duka.app.domain.TaxCalculator
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.duka.app.R
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TaxEbmScreen(
    businessRepository: BusinessRepository,
    saleRepository: SaleRepository,
    taxRepository: TaxRepository,
    ebmRepository: EbmRepository,
    onNavigateToSendEbm: (Long) -> Unit
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val businessId = business?.id ?: 0L

    // Get all-time total for tax tier computation
    var totalTurnover by remember { mutableDoubleStateOf(0.0) }
    LaunchedEffect(businessId) {
        if (businessId > 0) {
            totalTurnover = saleRepository.getTotalAllTime(businessId) ?: 0.0
            // Update tax profile
            val quarterlyTurnover = totalTurnover / 4.0
            val result = TaxCalculator.compute(quarterlyTurnover)
            taxRepository.upsert(
                com.duka.app.data.local.entity.TaxProfile(
                    businessId = businessId,
                    quarterlyTurnover = quarterlyTurnover,
                    tier = result.tier.label,
                    vatCollected = result.vatCollected
                )
            )
        }
    }

    val taxProfile by taxRepository.getTaxProfile(businessId).collectAsState(initial = null)
    val receipts by ebmRepository.getReceiptsByBusiness(businessId).collectAsState(initial = emptyList())

    val quarterlyTurnover = totalTurnover / 4.0
    val taxResult = TaxCalculator.compute(quarterlyTurnover)
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM d, HH:mm") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.TAX_EBM,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tax_title), color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tax tier card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {                            Text(stringResource(R.string.tax_tier),
                            style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant)
                        )
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            taxResult.tier.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            // Annual turnover
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(stringResource(R.string.tax_annual_turnover), style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                            Text(
                                "RWF ${currencyFormat.format(taxResult.annualTurnover)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Forest)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(stringResource(R.string.tax_est_annual), style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant))
                            Text(
                                "RWF ${currencyFormat.format(taxResult.estimatedTax)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Ink)
                            )
                        }
                    }
                }
            }

            // VAT collected this month
            if (taxResult.tier == TaxCalculator.TaxTier.FULL_VAT) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.tax_vat_quarterly), style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "RWF ${currencyFormat.format(taxResult.vatCollected)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Clay)
                            )
                        }
                    }
                }
            }

            // EBM receipts header
            item {
                Text(
                    stringResource(R.string.tax_recent_receipts),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (receipts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Text(
                            stringResource(R.string.tax_no_receipts),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            items(receipts) { receipt ->
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                receipt.receiptNumber,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                java.time.Instant.ofEpochMilli(receipt.sentAt).atZone(ZoneId.systemDefault())
                                    .format(dateFormatter),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // Status chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (receipt.status) {
                                "sent" -> Forest.copy(alpha = 0.1f)
                                else -> Amber.copy(alpha = 0.1f)
                            }
                        ) {
                            Text(
                                receipt.status.uppercase(),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = when (receipt.status) {
                                        "sent" -> Forest
                                        else -> Amber
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
}
