package com.duka.phase4.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import com.duka.phase3.data.RemoteShopProduct
import com.duka.phase4.sync.ClientPurchaseSyncService
import kotlinx.coroutines.launch
/**
 * Phase 4 — Purchase bottom sheet, opened by tapping a product on a Shop Profile.
 * Quantity selector, payment-method PLACEHOLDER (display only — same honesty as
 * the V3 client store: no real Mobile Money processing in this phase), and a Buy
 * button that records the purchase local-first and navigates to the receipt.
 *
 * OFFLINE HANDLING (binding spec): the record is always created (local-first);
 * whether the backend confirmed it is shown from syncState. When the push failed
 * the sheet says "Purchase pending — will complete when you're back online" and
 * the Buy button becomes "Done" — never a fake success, never a silent failure.
 *
 * DESIGN RULES: reuses DukaPrimaryButton + theme tokens only; no inline styling
 * beyond layout, no emoji icons (material icons only), matching Phase 3 sheets.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPurchaseSheet(
    product: RemoteShopProduct,
    shopRemoteId: String,
    shopName: String,
    clientUserId: Long,
    service: ClientPurchaseSyncService,
    onDismiss: () -> Unit,
    onPurchaseRecorded: (remoteId: String, pending: Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var quantity by remember { mutableIntStateOf(1) }
    var recording by remember { mutableStateOf(false) }
    var pendingOffline by remember { mutableStateOf(false) }
    var recordedRemoteId by remember { mutableStateOf("") }
    val syncState by service.syncState.collectAsState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { if (!recording) onDismiss() },
        sheetState = sheetState,
        containerColor = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                product.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            if (product.category.isNotBlank()) {
                Text(product.category, fontSize = 12.sp, color = OnSurfaceVariant)
            }
            Spacer(Modifier.height(14.dp))

            // Quantity selector — the one control this sheet exists for.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Quantity", fontSize = 14.sp, color = OnSurfaceVariant, modifier = Modifier.weight(1f))
                Surface(shape = RoundedCornerShape(10.dp), color = Mist) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            enabled = quantity > 1 && !recording
                        ) {
                            Icon(
                                Icons.Filled.Remove, contentDescription = "Decrease quantity",
                                tint = if (quantity > 1) Forest else OnSurfaceVariant
                            )
                        }
                        Text(
                            "$quantity",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(onClick = { if (quantity < 99) quantity++ }, enabled = quantity < 99 && !recording) {
                            Icon(
                                Icons.Filled.Add, contentDescription = "Increase quantity",
                                tint = if (quantity < 99) Forest else OnSurfaceVariant
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            // Payment method placeholder — display only, per phase scope.
            Text("Payment method", fontSize = 12.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = Mist, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Mobile Money •••• 214",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Forest
                )
            }
            Spacer(Modifier.height(16.dp))

            // Total
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Total", fontSize = 14.sp, color = OnSurfaceVariant, modifier = Modifier.weight(1f))
                Text(
                    "₣%,.0f".format(product.price * quantity),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Forest
                )
            }
            Spacer(Modifier.height(16.dp))

            // Offline / pending notice — visible, never silent.
            if (pendingOffline) {
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
                Spacer(Modifier.height(10.dp))
            }

            DukaPrimaryButton(
                text = when {
                    recording -> "Recording…"
                    pendingOffline -> "Done"
                    else -> "Buy"
                },
                onClick = {
                    if (pendingOffline) {
                        // Acknowledged the pending state — go to the receipt view.
                        onPurchaseRecorded(recordedRemoteId, true)
                    } else if (!recording) {
                        recording = true
                        val qty = quantity
                        val price = product.price
                        // Record local-first; syncState tells us if the push failed.
                        scope.launch {
                            val purchase = service.recordPurchase(
                                shopRemoteId = shopRemoteId,
                                shopName = shopName,
                                productName = product.name,
                                unitPrice = price,
                                quantity = qty,
                                clientUserId = clientUserId
                            )
                            recordedRemoteId = purchase.remoteId
                            pendingOffline = purchase.pendingPush || syncState.lastError != null
                            recording = false
                            if (!pendingOffline) onPurchaseRecorded(purchase.remoteId, false)
                        }
                    }
                },
                enabled = !recording
            )
        }
    }
}
