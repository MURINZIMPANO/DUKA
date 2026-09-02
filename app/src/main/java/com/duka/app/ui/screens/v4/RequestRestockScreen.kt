package com.duka.app.ui.screens.v4

/**
 * V4 Request Restock Screen — owner-facing restock request for low-stock products.
 *
 * SCOPE-GUARD: READS from V1/V2 Product and Sale tables (read-only).
 * WRITES to V4 RestockRequest table only. No writes to V1/V2 tables.
 * Uses ReorderQuantityCalculator pure function for suggested reorder quantities.
 *
 * MOCK STATUS: Wholesaler delivery estimates are locally-seeded. No real supply chain.
 * Restock requests are local-only. No real wholesaler integration.
 */

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.duka.app.data.local.entity.RestockRequest
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.RestockRequestRepository
import com.duka.app.data.repository.SaleRepository
import com.duka.app.data.repository.WholesalerRepository
import com.duka.app.domain.ReorderQuantityCalculator
import com.duka.app.ui.components.DukaPrimaryButton
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
import kotlinx.coroutines.launch

@Composable
fun RequestRestockScreen(
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    saleRepository: SaleRepository,
    wholesalerRepository: WholesalerRepository,
    restockRequestRepository: RestockRequestRepository,
    onBack: (() -> Unit)? = null
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val products by productRepository.getProductsByBusiness(business?.id ?: 0L)
        .collectAsState(initial = emptyList())
    val wholesalers by wholesalerRepository.getAll().collectAsState(initial = emptyList())
    val recentRequests by restockRequestRepository.getByBusiness(business?.id ?: 0L)
        .collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    val businessId = business?.id ?: 0L
    val lowStockThreshold = 10

    // Compute reorder suggestions for low-stock products
    var reorderSuggestions by remember { mutableStateOf<List<ReorderQuantityCalculator.ReorderSuggestion>>(emptyList()) }

    LaunchedEffect(products) {
        val suggestions = mutableListOf<ReorderQuantityCalculator.ReorderSuggestion>()
        for (product in products) {
            if (product.stockQuantity <= lowStockThreshold * 3) {
                // Get recent sales count (simplified: last 14 days)
                val now = System.currentTimeMillis()
                val twoWeeksAgo = now - (14L * 24 * 60 * 60 * 1000)
                val recentSales = saleRepository.getSalesCountInRange(businessId, twoWeeksAgo, now)
                    .let { flow ->
                        var count = 0
                        flow.collect { count = it }
                        count
                    }

                val suggestion = ReorderQuantityCalculator.compute(
                    productId = product.id,
                    productName = product.name,
                    currentStock = product.stockQuantity,
                    recentSalesCount = recentSales,
                    lowStockThreshold = lowStockThreshold
                )
                if (suggestion != null) suggestions.add(suggestion)
            }
        }
        reorderSuggestions = suggestions
    }

    var selectedWholesalerId by remember { mutableStateOf(0L) }
    var wholesalerExpanded by remember { mutableStateOf(false) }
    var requestSent by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.RESTOCK,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.restock_title), color = White) },
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
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Low-stock products
            item {
                Text(
                    "Low Stock Products",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                )
            }

            if (reorderSuggestions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Text(
                            "All products are well-stocked. No restock needed.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            items(reorderSuggestions) { suggestion ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                suggestion.productName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (suggestion.currentStock == 0) Clay.copy(alpha = 0.1f) else Amber.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    "${suggestion.currentStock} left",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (suggestion.currentStock == 0) Clay else Amber
                                    )
                                )
                            }
                        }
                        Text(
                            suggestion.reason,
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Suggested reorder: ${suggestion.suggestedQuantity} units",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Forest, fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            // Wholesaler picker
            if (reorderSuggestions.isNotEmpty()) {
                item {
                    Text(
                        "Select Wholesaler",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Ink)
                    )

                    ExposedDropdownMenuBox(
                        expanded = wholesalerExpanded,
                        onExpandedChange = { wholesalerExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = wholesalers.find { it.id == selectedWholesalerId }?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Wholesaler") },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Forest,
                                unfocusedBorderColor = Mist,
                                focusedContainerColor = White,
                                unfocusedContainerColor = White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = wholesalerExpanded,
                            onDismissRequest = { wholesalerExpanded = false }
                        ) {
                            wholesalers.forEach { wholesaler ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(wholesaler.name)
                                            Text(
                                                "${wholesaler.specialty} \u2022 ${wholesaler.deliveryEstimate}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedWholesalerId = wholesaler.id
                                        wholesalerExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    DukaPrimaryButton(
                        text = "Send restock request",
                        onClick = {
                            scope.launch {
                                for (suggestion in reorderSuggestions) {
                                    restockRequestRepository.createRequest(
                                        RestockRequest(
                                            businessId = businessId,
                                            productId = suggestion.productId,
                                            wholesalerId = selectedWholesalerId,
                                            quantity = suggestion.suggestedQuantity,
                                            status = "requested"
                                        )
                                    )
                                }
                                requestSent = true
                            }
                        },
                        enabled = selectedWholesalerId > 0 && !requestSent
                    )
                }
            }

            // Recent requests
            if (recentRequests.isNotEmpty()) {
                item {
                    Text(
                        "Recent Requests",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    )
                }

                items(recentRequests.take(5)) { request ->
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
                                    "Product #${request.productId}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    "${request.quantity} units",
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = when (request.status) {
                                    "delivered" -> Forest.copy(alpha = 0.1f)
                                    "in_transit" -> Amber.copy(alpha = 0.1f)
                                    else -> Mist
                                }
                            ) {
                                Text(
                                    request.status.replace("_", " ").replaceFirstChar { it.uppercase() },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = when (request.status) {
                                            "delivered" -> Forest
                                            "in_transit" -> Amber
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
    }
}
