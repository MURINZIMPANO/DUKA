package com.duka.phase4.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import com.duka.phase4.data.ClientPurchase
import com.duka.phase4.sync.ClientPurchaseSyncService
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Phase 4 — Instant EBM receipt shown right after a client purchase.
 *
 * MOCK STATUS (honesty comment preserved): uses the same MockEbmGateway as
 * V1/V2/V3 — ONE gateway, no second parallel mock. This is a local mock, NOT
 * real RRA transmission; a real integration replaces MockEbmGateway behind the
 * same EbmGateway interface later.
 *
 * Motion: checkmark scale-in matches the existing motion polish standard
 * (spring scale, same as ClientReceiveEbmScreen). Design tokens only.
 *
 * PENDING STATE: when the backend push failed (offline), the receipt renders
 * with a visible "pending" banner instead of pretending everything synced.
 */
@Composable
fun ClientPurchaseReceiptScreen(
    purchaseRemoteId: String,
    service: ClientPurchaseSyncService,
    ebmGateway: com.duka.app.domain.EbmGateway,
    onDone: () -> Unit
) {
    val purchase by service.observePurchase(purchaseRemoteId)
        .collectAsState(initial = null)
    var receiptNumber by remember { mutableStateOf(purchase?.receiptNumber ?: "") }
    var checked by remember { mutableStateOf(false) }
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }
    val syncState by service.syncState.collectAsState()

    // Scale-in checkmark — same spring recipe as the existing receipt screens.
    val successScale by animateFloatAsState(
        targetValue = if (checked) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "phase4ReceiptScale"
    )

    // Trigger the EBM mock exactly once for this purchase (idempotent-ish: the
    // number is stored on the row so re-entry reuses it rather than minting a new one).
    LaunchedEffect(purchaseRemoteId) {
        val p = service.getPurchase(purchaseRemoteId) ?: return@LaunchedEffect
        if (p.receiptNumber.isBlank()) {
            val result = ebmGateway.sendReceipt(0L, p.total, p.productName)
            if (result.success) {
                receiptNumber = result.receiptNumber
                service.saveReceiptNumber(purchaseRemoteId, result.receiptNumber)
            }
        } else {
            receiptNumber = p.receiptNumber
        }
        checked = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(com.duka.app.ui.theme.Canvas)
    ) {
        DecorativeBackdrop()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(successScale)
                    .background(Forest, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Check, contentDescription = "Purchase successful",
                    tint = White, modifier = Modifier.size(52.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "Purchase successful",
                fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Ink
            )
            Text(
                "Instant EBM receipt",
                fontSize = 13.sp, color = OnSurfaceVariant
            )
            Spacer(Modifier.height(22.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    ReceiptRow("Product", purchase?.productName ?: "—")
                    ReceiptRow("Quantity", "${purchase?.quantity ?: 0}")
                    ReceiptRow(
                        "Total",
                        "₣%,.0f".format(purchase?.total ?: 0.0),
                        valueColor = Forest,
                        bold = true
                    )
                    ReceiptRow("Shop", purchase?.shopName ?: "—")
                    ReceiptRow("Receipt no.", receiptNumber.ifBlank { "generating…" })
                    ReceiptRow(
                        "Time",
                        purchase?.timestamp?.takeIf { it > 0 }?.let {
                            SimpleDateFormat("d MMM yyyy · HH:mm", Locale.getDefault()).format(Date(it))
                        } ?: "—"
                    )
                }
            }

            // Visible pending-sync state — never silent, retriable on next sync.
            val showPending = (purchase?.pendingPush ?: false) || syncState.lastError != null
            if (showPending) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Clay.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.CloudOff, contentDescription = null,
                            tint = Clay, modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Purchase pending — will complete when you're back online",
                            fontSize = 12.sp, color = Clay
                        )
                    }
                }
            }

            Spacer(Modifier.height(26.dp))
            DukaPrimaryButton(
                text = "Done",
                onClick = onDone
            )
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = Ink,
    bold: Boolean = false
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)) {
        Text(label, fontSize = 13.sp, color = OnSurfaceVariant, modifier = Modifier.weight(1f))
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
            color = valueColor
        )
    }
}
