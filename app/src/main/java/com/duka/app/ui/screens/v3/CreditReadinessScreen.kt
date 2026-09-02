package com.duka.app.ui.screens.v3

/**
 * V3 Credit Readiness Screen — owner-facing credit score computation.
 *
 * SCOPE-GUARD: READS from V1/V2 Sale and EbmReceipt tables (read-only).
 * Uses CreditScoreCalculator pure function. No writes to any table.
 *
 * MOCK STATUS: Credit score is computed from local seed data. "Connect to lending partner"
 * shows a "Coming soon" state. No real lending integration.
 */

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.EbmRepository
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.data.repository.SaleRepository
import com.duka.app.domain.CreditScoreCalculator
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
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
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun CreditReadinessScreen(
    businessRepository: BusinessRepository,
    saleRepository: SaleRepository,
    ebmRepository: EbmRepository,
    onBack: (() -> Unit)? = null
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val businessId = business?.id ?: 0L

    var creditResult by remember { mutableStateOf<CreditScoreCalculator.CreditBreakdown?>(null) }

    LaunchedEffect(businessId) {
        if (businessId > 0) {
            // Gather data for credit score computation
            val now = System.currentTimeMillis()
            val monthStart = LocalDate.now().withDayOfMonth(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            // Count months with sales (simplified: check last 6 months)
            var monthsWithSales = 0
            val monthlyIncomes = mutableListOf<Double>()
            for (i in 0 until 6) {
                val start = LocalDate.now().minusMonths(i.toLong()).withDayOfMonth(1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val end = LocalDate.now().minusMonths(i.toLong() - 1).withDayOfMonth(1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val total = saleRepository.getTotalInRangeOnce(businessId, start, end) ?: 0.0
                if (total > 0) monthsWithSales++
                monthlyIncomes.add(total)
            }

            val totalSales = saleRepository.getSalesCountInRange(businessId, 0L, now)
                .let { flow ->
                    var count = 0
                    flow.collect { count = it }
                    count
                }

            // Count EBM receipts
            val receipts = ebmRepository.getReceiptsByBusiness(businessId)
                .let { flow ->
                    var list = emptyList<com.duka.app.data.local.entity.EbmReceipt>()
                    flow.collect { list = it }
                    list.size
                }

            val businessAgeMonths = 6 // Simplified for demo

            creditResult = CreditScoreCalculator.compute(
                totalMonths = businessAgeMonths,
                monthsWithSales = monthsWithSales,
                monthlyIncomes = monthlyIncomes.reversed(),
                totalSales = totalSales,
                totalReceipts = receipts
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.CREDIT,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.credit_title), color = White) },
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
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Credit score display
            creditResult?.let { result ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Forest)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Your Credit Score",
                                style = MaterialTheme.typography.labelMedium.copy(color = White.copy(alpha = 0.7f))
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Score circle
                            Surface(
                                modifier = Modifier.size(80.dp),
                                shape = CircleShape,
                                color = White
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${result.score}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Forest
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                result.label,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = White,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Score bar
                            LinearProgressIndicator(
                                progress = result.score / 100f,
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = White,
                                trackColor = White.copy(alpha = 0.3f),
                            )
                        }
                    }
                }

                // Breakdown
                item {
                    Text(
                        "What's driving your score",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    )
                }

                items(result.factors) { factor ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Text(
                            factor,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Recommendation
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Amber.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "Tip",
                                tint = Amber,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                result.recommendation,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Ink)
                            )
                        }
                    }
                }

                // Connect to lending partner (Coming soon)
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Mist)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Connect to a lending partner",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "Coming soon \u2014 no lending partners integrated yet. Your credit score will be shared with verified lenders once partnerships are established.",
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                            )
                            DukaPrimaryButton(
                                text = "Coming soon",
                                onClick = { },
                                enabled = false
                            )
                        }
                    }
                }
            }

            // Loading state
            if (creditResult == null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Text(
                            "Computing your credit score...",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }
        }
    }
    }
}
