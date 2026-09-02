package com.duka.products.management

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.duka.app.data.local.entity.Product
import com.duka.app.data.local.entity.StockAdjustment
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.StockAdjustmentRepository
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private data class CategoryOption(val label: String, val icon: ImageVector)

private val CATEGORIES = listOf(
    CategoryOption("Food", Icons.Outlined.Fastfood),
    CategoryOption("Beverages", Icons.Outlined.LocalDrink),
    CategoryOption("Household", Icons.Outlined.LocalGroceryStore),
    CategoryOption("Personal Care", Icons.Outlined.Checkroom),
    CategoryOption("Electronics", Icons.Outlined.ElectricalServices),
    CategoryOption("Other", Icons.Outlined.Storefront)
)

@Composable
fun EditProductScreen(
    productId: Long,
    businessId: Long,
    productRepository: ProductRepository,
    stockAdjustmentRepository: StockAdjustmentRepository,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var product by remember { mutableStateOf<Product?>(null) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf("") }
    var stockQuantity by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var lowStockThreshold by remember { mutableIntStateOf(5) }
    var isSaved by remember { mutableStateOf(false) }
    var isHighlighted by remember { mutableStateOf(false) }

    // Load product data
    LaunchedEffect(productId) {
        product = productRepository.getProductById(productId)
        product?.let { p ->
            name = p.name
            price = p.price.toLong().toString()
            costPrice = if (p.costPrice > 0) p.costPrice.toString() else ""
            stockQuantity = p.stockQuantity
            selectedCategory = p.category
            lowStockThreshold = p.lowStockThreshold
        }
    }

    // Highlight animation after save
    val highlightColor by animateColorAsState(
        targetValue = if (isHighlighted) Forest.copy(alpha = 0.08f) else Color.Transparent,
        animationSpec = tween(400),
        label = "highlight"
    )

    LaunchedEffect(isHighlighted) {
        if (isHighlighted) {
            delay(400)
            isHighlighted = false
        }
    }

    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Editing",
                            style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                        )
                        Text(
                            name.take(24) + if (name.length > 24) "…" else "",
                            color = White,
                            style = MaterialTheme.typography.headlineSmall
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
                .background(if (isHighlighted) highlightColor else Canvas)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )

                // Price field
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Sale price (RWF)") },
                    placeholder = { Text("Enter price") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )

                // Cost price field
                OutlinedTextField(
                    value = costPrice,
                    onValueChange = { costPrice = it.filter { c -> c.isDigit() } },
                    label = { Text("Cost price (RWF)") },
                    placeholder = { Text("What you pay your supplier") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    ),
                    supportingText = {
                        Text(
                            "Used to calculate profit",
                            style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                        )
                    }
                )

                // Stock quantity stepper
                Text(
                    "Stock quantity",
                    style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (stockQuantity > 0) stockQuantity-- },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("−", style = MaterialTheme.typography.headlineMedium, color = Forest)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Mist,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "$stockQuantity",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Forest
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                    IconButton(
                        onClick = { stockQuantity++ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.headlineMedium, color = Forest)
                    }
                }

                // Category picker
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                        leadingIcon = {
                            val selectedOption = CATEGORIES.find { it.label == selectedCategory }
                            Icon(
                                selectedOption?.icon ?: Icons.Outlined.Storefront,
                                contentDescription = null,
                                tint = Forest
                            )
                        },
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
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        CATEGORIES.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                leadingIcon = {
                                    Icon(option.icon, contentDescription = null, tint = Forest)
                                },
                                onClick = {
                                    selectedCategory = option.label
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // Low stock threshold
                Text(
                    "Alert me when below [N] units",
                    style = MaterialTheme.typography.labelMedium.copy(color = OnSurfaceVariant)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (lowStockThreshold > 1) lowStockThreshold-- },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("−", style = MaterialTheme.typography.headlineMedium, color = Forest)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Mist,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "$lowStockThreshold",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Forest
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                    IconButton(
                        onClick = { lowStockThreshold++ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.headlineMedium, color = Forest)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Save button
                Button(
                    onClick = {
                        val priceValue = price.toDoubleOrNull() ?: return@Button
                        val costValue = costPrice.toLongOrNull() ?: 0L
                        val originalStock = product?.stockQuantity ?: stockQuantity
                        val stockDelta = stockQuantity - originalStock

                        scope.launch {
                            product?.let { p ->
                                productRepository.updateProduct(
                                    p.copy(
                                        name = name.trim(),
                                        price = priceValue,
                                        costPrice = costValue,
                                        stockQuantity = stockQuantity,
                                        category = selectedCategory,
                                        lowStockThreshold = lowStockThreshold
                                    )
                                )

                                // Create stock adjustment if stock changed manually
                                if (stockDelta != 0) {
                                    stockAdjustmentRepository.create(
                                        StockAdjustment(
                                            productId = p.id,
                                            businessId = businessId,
                                            delta = stockDelta,
                                            reason = "manual_edit",
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                }

                                isSaved = true
                                isHighlighted = true
                                delay(1500)
                                isSaved = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Forest,
                        contentColor = White
                    )
                ) {
                    Text(
                        "Save changes",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                if (isSaved) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Forest.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Changes saved!",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                        )
                    }
                }
            }
        }
    }
}
