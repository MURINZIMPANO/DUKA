package com.duka.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.duka.app.R
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.ProductAlertRepository
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.duka.app.ui.components.ShimmerBox
import kotlinx.coroutines.flow.combine
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    productAlertRepository: ProductAlertRepository? = null,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (Long) -> Unit = {}
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val businessId = business?.id ?: 0L

    // Search query state
    var searchQuery by remember { mutableStateOf("") }

    // Category filter state
    var selectedCategory by remember { mutableStateOf("All") }

    // Distinct categories from the product table
    val categories by productRepository.getCategoriesByBusiness(businessId)
        .collectAsState(initial = emptyList())

    // Combined search + category filter via ViewModel-level combine (not in Composable filter)
    val allProducts by productRepository.getProductsByBusiness(businessId)
        .collectAsState(initial = emptyList())

    // V6: Alert chips per product
    var productAlertsMap by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }
    LaunchedEffect(allProducts, businessId) {
        if (productAlertRepository != null && businessId > 0) {
            productAlertRepository.getUnreadByBusiness(businessId).collect { alerts ->
                productAlertsMap = alerts.associate { it.productId to it.type }
            }
        }
    }

    // Delete state
    var productToDelete by remember { mutableStateOf<com.duka.app.data.local.entity.Product?>(null) }

    // === Phase 3: Category quick-start offer (additive) ===
    // Shown once per session when the shop has NO products and its category has
    // a starter template. Declining inserts nothing; existing behaviour otherwise.
    var showQuickStart by remember { mutableStateOf(false) }
    var quickStartChecked by remember { mutableStateOf(false) }
    LaunchedEffect(allProducts.isEmpty(), business?.type) {
        if (allProducts.isEmpty() && !business?.type.isNullOrBlank() && !quickStartChecked) {
            quickStartChecked = true
            if (com.duka.phase3.data.CategoryTemplates.hasTemplate(business?.type ?: "")) {
                showQuickStart = true
            }
        }
    }
    if (showQuickStart && businessId > 0L) {
        com.duka.phase3.ui.CategoryQuickStartDialog(
            category = business?.type ?: "",
            businessId = businessId,
            productRepository = productRepository,
            onDismiss = { showQuickStart = false }
        )
    }
    // === End Phase 3 hook ===

    val filteredProducts = remember(allProducts, searchQuery, selectedCategory) {
        allProducts.filter { product ->
            val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery.trim(), ignoreCase = true)
            val matchesCategory = selectedCategory == "All" ||
                product.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }

    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    // Track whether initial data has loaded
    var dataLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(business?.id) {
        if (business?.id != null) dataLoaded = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.products_title), color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddProduct,
                containerColor = Forest,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search your products…") },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Outlined.Close, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            // Category filter chips
            val allCategories = listOf("All") + categories
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allCategories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Forest,
                            selectedLabelColor = White,
                            containerColor = White,
                            labelColor = Ink
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Mist,
                            selectedBorderColor = Forest
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!dataLoaded) {
                // Shimmer loading state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(5) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(72.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            } else if (filteredProducts.isEmpty() && searchQuery.isNotBlank()) {
                // Search-specific empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.SearchOff,
                            contentDescription = "No results",
                            modifier = Modifier.size(48.dp),
                            tint = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No products matching \"$searchQuery\"",
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            } else if (filteredProducts.isEmpty()) {
                // Generic empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.Inventory2,
                            contentDescription = "No products",
                            modifier = Modifier.size(48.dp),
                            tint = OnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No products yet",
                            style = MaterialTheme.typography.headlineSmall.copy(color = OnSurfaceVariant)
                        )
                        Text(
                            "Tap + to add your first product",
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(filteredProducts) { index, product ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay((index * 30).toLong())
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(300)) + slideInVertically(
                                tween(300, easing = FastOutSlowInEasing)
                            )
                        ) {
                            var showMenu by remember { mutableStateOf(false) }
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            product.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                        )
                                        Text(
                                            product.category,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        // Alert chip
                                        productAlertsMap[product.id]?.let { alertType ->
                                            val chipText = when (alertType) {
                                                "low_stock" -> "Low stock"
                                                "slow_mover" -> "Slow mover"
                                                "restock_suggestion" -> "Restock soon"
                                                else -> null
                                            }
                                            val chipColor = when (alertType) {
                                                "low_stock" -> Clay
                                                else -> Amber
                                            }
                                            if (chipText != null) {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = chipColor.copy(alpha = 0.1f)
                                                ) {
                                                    Text(
                                                        chipText,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                        style = MaterialTheme.typography.labelSmall.copy(color = chipColor)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                "RWF ${currencyFormat.format(product.price)}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Forest
                                                )
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = if (product.stockQuantity > 0) Forest.copy(alpha = 0.1f) else Clay.copy(alpha = 0.1f)
                                            ) {
                                                Text(
                                                    "${product.stockQuantity} in stock",
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = if (product.stockQuantity > 0) Forest else Clay
                                                    )
                                                )
                                            }
                                        }
                                        Box {
                                            IconButton(onClick = { showMenu = true }) {
                                                Icon(
                                                    Icons.Outlined.MoreVert,
                                                    contentDescription = "More options",
                                                    tint = OnSurfaceVariant
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = showMenu,
                                                onDismissRequest = { showMenu = false }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text("Edit product") },
                                                    leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null, tint = Forest) },
                                                    onClick = {
                                                        showMenu = false
                                                        onNavigateToEditProduct(product.id)
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Delete product", color = Clay) },
                                                    leadingIcon = { Icon(Icons.Outlined.DeleteOutline, contentDescription = null, tint = Clay) },
                                                    onClick = {
                                                        showMenu = false
                                                        productToDelete = product
                                                    }
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
        }
    }

    // Delete confirmation dialog
    val deleteScope = rememberCoroutineScope()
    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Delete ${product.name}?") },
            text = { Text("This will permanently remove the product and its stock record. Sales history will be kept.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteScope.launch {
                            productRepository.softDelete(product.id)
                            productToDelete = null
                        }
                    }
                ) {
                    Text("Delete", color = Clay)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
