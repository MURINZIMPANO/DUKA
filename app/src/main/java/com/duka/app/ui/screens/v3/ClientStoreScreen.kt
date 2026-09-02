package com.duka.app.ui.screens.v3

/**
 * Client Store Screen (C2) — single product focus, matching prototype.
 *
 * SCOPE-GUARD: READS from V1/V2 Product and Business tables (read-only).
 * WRITES to V3 Purchase table and V1/V2 Sale table (new "client" source value).
 * The Sale write is the only V1/V2 write from V3 — it adds a new source value ("client"),
 * it does not modify existing Sale data.
 *
 * MOCK STATUS: Payment method is a placeholder ("Mobile Money — •••• 214").
 * No real payment integration. EBM receipt uses the same MockEbmGateway as V1/V2.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.Product
import com.duka.app.data.local.entity.Purchase
import com.duka.app.data.local.entity.Sale
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.PurchaseRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ClientStoreScreen(
    businessId: Long,
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    saleRepository: SaleRepository,
    purchaseRepository: PurchaseRepository,
    clientUserId: Long,
    onBack: () -> Unit,
    onPurchaseComplete: (saleId: Long) -> Unit
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val products by productRepository.getProductsByBusiness(businessId)
        .collectAsState(initial = emptyList())
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        // Decorative backdrop
        DecorativeBackdrop(
            page = DukaPage.CLIENT_STORE,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                business?.name ?: "Store",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                selectedProduct?.name ?: "Store",
                                color = White,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
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
                // Top product card (if a product is selected, show it; otherwise show first)
                val displayProduct = selectedProduct ?: products.firstOrNull()

                if (displayProduct != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                // Price in monospace
                                Text(
                                    "${currencyFormat.format(displayProduct.price.toLong())} RWF",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Forest
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Status chip
                                val stockStatus = when {
                                    displayProduct.stockQuantity > 10 -> "In stock" to Forest
                                    displayProduct.stockQuantity > 0 -> "Low stock" to Amber
                                    else -> "Out of stock" to Clay
                                }
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = stockStatus.second.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        stockStatus.first,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = stockStatus.second,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Payment method field
                                Text(
                                    "Payment method",
                                    style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant)
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Mist,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Mobile Money \u2022\u2022\u2022\u2022 214",
                                        modifier = Modifier.padding(12.dp),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = Forest
                                        )
                                    )
                                }

                                // TODO: real payment integration (MTN MoMo / Airtel Money API) in a future phase — do not imply this is live

                                Spacer(modifier = Modifier.height(16.dp))

                                // Buy button
                                DukaPrimaryButton(
                                    text = "Buy from this store",
                                    onClick = {
                                        val product = displayProduct
                                        if (product == null || product.stockQuantity <= 0) {
                                            showError = "Product is out of stock"
                                            return@DukaPrimaryButton
                                        }
                                        CoroutineScope(Dispatchers.IO).launch {
                                            // 1. Decrement stock
                                            val stockDecremented = productRepository.decrementStock(product.id)
                                            if (!stockDecremented) {
                                                showError = "Out of stock — someone else may have bought the last one"
                                                return@launch
                                            }

                                            // 2. Create Sale with source = "client"
                                            val saleId = saleRepository.createSale(
                                                Sale(
                                                    productId = product.id,
                                                    businessId = businessId,
                                                    amount = product.price,
                                                    source = "client"
                                                )
                                            )

                                            // 3. Create Purchase row
                                            purchaseRepository.createPurchase(
                                                Purchase(
                                                    clientUserId = clientUserId,
                                                    businessId = businessId,
                                                    productId = product.id,
                                                    amount = product.price
                                                )
                                            )

                                            // 4. Trigger MockEbmGateway (called on the receive EBM screen)

                                            showError = null
                                            delay(300)
                                            onPurchaseComplete(saleId)
                                        }
                                    },
                                    enabled = displayProduct.stockQuantity > 0
                                )

                                // Inline error
                                showError?.let { msg ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Clay.copy(alpha = 0.1f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            msg,
                                            modifier = Modifier.padding(10.dp),
                                            style = MaterialTheme.typography.bodySmall.copy(color = Clay)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // "You might also like" section
                val suggestions = products.filter {
                    it.id != displayProduct?.id && it.stockQuantity > 0
                }.take(4)

                if (suggestions.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "You might also like",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        )
                    }

                    items(suggestions) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedProduct = product },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        product.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                    // Stock chip
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Forest.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            "In stock",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(color = Forest)
                                        )
                                    }
                                }
                                Text(
                                    "RWF ${currencyFormat.format(product.price.toLong())}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Forest
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
