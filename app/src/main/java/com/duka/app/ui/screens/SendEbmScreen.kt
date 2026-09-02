package com.duka.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.EbmReceipt
import com.duka.app.data.repository.EbmRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.app.domain.EbmGateway
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SendEbmScreen(
    saleId: Long,
    saleRepository: SaleRepository,
    productRepository: ProductRepository,
    ebmRepository: EbmRepository,
    ebmGateway: EbmGateway,
    onDismiss: () -> Unit
) {
    val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
    var status by remember { mutableStateOf("Sending...") }
    var receiptNumber by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var amount by remember { mutableDoubleStateOf(0.0) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isSent by remember { mutableStateOf(false) }
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    // Scale animation for the confirmation card
    val cardScale by animateFloatAsState(
        targetValue = if (status != "Sending...") 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    LaunchedEffect(saleId) {
        // Look up sale details
        val sale = saleRepository.getSaleById(saleId)
        if (sale != null) {
            amount = sale.amount
            val product = productRepository.getProductById(sale.productId)
            productName = product?.name ?: "Sale"
        }

        scope.launch {
            val startTime = System.currentTimeMillis()
            val result = ebmGateway.sendReceipt(saleId, amount, productName)
            val endTime = System.currentTimeMillis()
            elapsedSeconds = ((endTime - startTime) / 1000).toInt().coerceAtLeast(1)

            if (result.success) {
                receiptNumber = result.receiptNumber
                status = "Sent · ${elapsedSeconds}s"
                isSent = true
                // Save receipt locally
                ebmRepository.saveReceipt(
                    EbmReceipt(
                        saleId = saleId,
                        receiptNumber = receiptNumber,
                        status = "sent"
                    )
                )
            } else {
                status = "Failed · ${result.message}"
            }

            // Auto-dismiss after 3 seconds
            delay(3000)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = status != "Sending...",
            enter = scaleIn(tween(300, easing = FastOutSlowInEasing)) + fadeIn(tween(300)),
            exit = fadeOut(tween(200))
        ) {
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .scale(cardScale),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isSent) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Sent",
                            modifier = Modifier.size(48.dp),
                            tint = Forest
                        )
                    } else {
                        Icon(
                            Icons.Filled.Error,
                            contentDescription = "Failed",
                            modifier = Modifier.size(48.dp),
                            tint = Clay
                        )
                    }

                    Text(
                        text = status,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSent) Forest else Clay
                        )
                    )

                    if (receiptNumber.isNotBlank()) {
                        Text(
                            text = receiptNumber,
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                        )
                    }

                    if (amount > 0) {
                        Text(
                            text = if (productName.isNotBlank()) "$productName — RWF ${currencyFormat.format(amount)}"
                                   else "Sale #$saleId — RWF ${currencyFormat.format(amount)}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Ink)
                        )
                    }
                }
            }
        }

        // Show spinner while sending
        if (status == "Sending...") {
            CircularProgressIndicator(
                color = Forest,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
