package com.duka.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.local.entity.Product
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DecorativeDensity
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R
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
fun AddProductScreen(
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    onBack: () -> Unit
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stockQuantity by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    val recentProducts by productRepository.getRecentProducts(business?.id ?: 0L)
        .collectAsState(initial = emptyList())

    val isValid = name.isNotBlank() && price.isNotBlank() && stockQuantity.isNotBlank() && selectedCategory.isNotBlank()
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_product_title), color = White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(Canvas)) {
        // V3/V4: Decorative backdrop for Add Product screen
        DecorativeBackdrop(
            density = DecorativeDensity.Low,
            seed = 5,
            modifier = Modifier.fillMaxSize()
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.field_product_name)) },
                    placeholder = { Text(stringResource(R.string.field_product_name_hint)) },
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

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text(stringResource(R.string.field_price)) },
                    placeholder = { Text(stringResource(R.string.field_price_hint)) },
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

                OutlinedTextField(
                    value = stockQuantity,
                    onValueChange = { stockQuantity = it.filter { c -> c.isDigit() } },
                    label = { Text(stringResource(R.string.field_stock_quantity)) },
                    placeholder = { Text(stringResource(R.string.field_stock_hint)) },
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

                // Icon-labeled category picker (per spec)
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.field_category)) },
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

                Spacer(modifier = Modifier.height(8.dp))

                DukaPrimaryButton(
                    text = stringResource(R.string.add_product_save),
                    onClick = {
                        val priceValue = price.toDoubleOrNull() ?: return@DukaPrimaryButton
                        val stockValue = stockQuantity.toIntOrNull() ?: return@DukaPrimaryButton
                        val businessId = business?.id ?: return@DukaPrimaryButton

                        scope.launch {
                            productRepository.addProduct(
                                Product(
                                    businessId = businessId,
                                    name = name.trim(),
                                    price = priceValue,
                                    stockQuantity = stockValue,
                                    category = selectedCategory
                                )
                            )
                            name = ""
                            price = ""
                            stockQuantity = ""
                            selectedCategory = ""
                            showSuccess = true
                        }
                    },
                    enabled = isValid
                )

                if (showSuccess) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Forest.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(R.string.add_product_saved),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                        )
                    }
                }

                Text(
                    stringResource(R.string.add_product_recent),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Staggered animation for recently added products
            itemsIndexed(recentProducts) { index, product ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay((index * 40).toLong())
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(300)) + slideInVertically(
                        tween(300, easing = FastOutSlowInEasing)
                    )
                ) {
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
                                Text(product.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                                Text(product.category, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                "RWF ${currencyFormat.format(product.price)}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Forest
                                )
                            )
                        }
                    }
                }
            }
        } // close LazyColumn
        } // close Box
    }
}
