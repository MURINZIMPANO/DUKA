package com.duka.app.ui.screens.v3

/**
 * Client Receive EBM Screen (C3) — "Receipt received"
 *
 * SCOPE-GUARD: READS from V1/V2 Sale and Product tables. Uses the same MockEbmGateway
 * as V1/V2 — one gateway, two perspectives. No separate mock implementation.
 *
 * MOCK STATUS: Uses MockEbmGateway for receipt generation. No real EBM system.
 */

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.app.domain.EbmGateway
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ClientReceiveEbmScreen(
    saleId: Long,
    saleRepository: SaleRepository,
    productRepository: ProductRepository,
    ebmGateway: EbmGateway,
    onDismiss: () -> Unit
) {
    var status by remember { mutableStateOf("Receiving...") }
    var receiptNumber by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(0.0) }
    var isReceived by remember { mutableStateOf(false) }
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    // Scale animation for the success indicator
    val successScale by animateFloatAsState(
        targetValue = if (isReceived) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "successScale"
    )

    LaunchedEffect(saleId) {
        val sale = saleRepository.getSaleById(saleId)
        if (sale != null) {
            amount = sale.amount
            val product = productRepository.getProductById(sale.productId)
            productName = product?.name ?: "Purchase"
        }

        // Reuse the same MockEbmGateway as V1/V2 — one gateway, two perspectives
        val result = ebmGateway.sendReceipt(saleId, amount, productName)

        if (result.success) {
            receiptNumber = result.receiptNumber
            status = "Received"
            isReceived = true
        } else {
            status = "Failed"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        // Decorative backdrop
        DecorativeBackdrop(
            page = DukaPage.CLIENT_EBM,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Instant \u00b7 Auto",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                "Receipt received",
                                color = White,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    // No back arrow — this is a confirmation, not a place to go back from
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Large success indicator — circular Forest background + white check
                if (isReceived) {
                    Surface(
                        modifier = Modifier
                            .size(64.dp)
                            .scale(successScale),
                        shape = CircleShape,
                        color = Forest
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Received",
                                modifier = Modifier.size(32.dp),
                                tint = White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Receipt card
                if (isReceived) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Product name — price
                            Text(
                                "$productName \u2014 ${currencyFormat.format(amount.toLong())} RWF",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )

                            // EBM receipt # — receipt number in Amber monospace
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "EBM receipt #",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                                )
                                Text(
                                    receiptNumber,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Amber
                                    )
                                )
                            }

                            // Received — Just now
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Received",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                                )
                                Text(
                                    "Just now",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtext
                    Text(
                        "Sent the instant the shop confirms your purchase \u2014 nothing to request, nothing to wait on.",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Done button — routes back to Client Discover, clearing back stack
                    DukaPrimaryButton(
                        text = "Done",
                        onClick = onDismiss
                    )
                }

                // Loading state
                if (!isReceived && status == "Receiving...") {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Mist,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Receiving receipt...",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }
        }
    }
}
