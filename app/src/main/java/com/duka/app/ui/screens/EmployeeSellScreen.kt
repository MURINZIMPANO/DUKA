package com.duka.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.local.entity.Sale
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.data.repository.SaleRepository
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EmployeeSellScreen(
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    saleRepository: SaleRepository,
    appNotificationRepository: com.duka.app.data.repository.AppNotificationRepository? = null,
    employeeUserId: Long = 0L,
    employeeBusinessId: Long = 0L,
    employeeName: String = "Employee",
    onLogout: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {}
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var showSoldConfirmation by remember { mutableStateOf<String?>(null) }

    // Track animated stock quantities for smooth decrement animation
    var animatedStocks by remember { mutableStateOf<Map<Long, Int>>(emptyMap()) }

    val businessId = business?.id ?: 0L
    val products by if (searchQuery.isBlank()) {
        productRepository.getProductsByBusiness(businessId)
    } else {
        productRepository.searchProducts(businessId, searchQuery)
    }.collectAsState(initial = emptyList())

    // Update animated stocks when products change
    LaunchedEffect(products) {
        animatedStocks = products.associate { it.id to it.stockQuantity }
    }

    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.SELL,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.sell_title), color = White) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = White
                        )
                    }
                    IconButton(onClick = onNavigateToNotifications) {
                        Box {
                            Icon(
                                Icons.Filled.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.sell_search_hint)) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
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

            // Product list with staggered entry
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(products) { index, product ->
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
                        val displayStock = animatedStocks[product.id] ?: product.stockQuantity
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
                                        "RWF ${currencyFormat.format(product.price)}",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                                    )
                                    // Animated stock count
                                    AnimatedContent(
                                        targetState = displayStock,
                                        transitionSpec = {
                                            fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                                        },
                                        label = "stockAnimation"
                                    ) { stock ->
                                        Text(
                                            "$stock in stock",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (stock > 0) OnSurfaceVariant else Clay
                                            )
                                        )
                                    }
                                }
                                Button(
                                    onClick = {
                                        if (displayStock > 0) {
                                            scope.launch {
                                                // Optimistic UI update
                                                animatedStocks = animatedStocks.toMutableMap().apply {
                                                    put(product.id, (animatedStocks[product.id] ?: product.stockQuantity) - 1)
                                                }

                                                val sold = productRepository.decrementStock(product.id)
                                                if (sold) {
                                                    val saleId = saleRepository.createSale(
                                                        Sale(
                                                            productId = product.id,
                                                            businessId = businessId,
                                                            amount = product.price,
                                                            source = "employee"
                                                        )
                                                    )
                                                    showSoldConfirmation = "${product.name} — RWF ${currencyFormat.format(product.price)}"
                                                } else {
                                                    // Revert on failure
                                                    animatedStocks = animatedStocks.toMutableMap().apply {
                                                        put(product.id, (animatedStocks[product.id] ?: 0) + 1)
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    enabled = displayStock > 0,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (displayStock > 0) Forest else Mist,
                                        contentColor = if (displayStock > 0) White else OnSurfaceVariant,
                                        disabledContainerColor = Mist,
                                        disabledContentColor = OnSurfaceVariant
                                    ),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(stringResource(R.string.sell_button))
                                }
                            }
                        }
                    }
                }
            }

            // Sold confirmation toast
            showSoldConfirmation?.let { msg ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Forest
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(msg, color = White, style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = { showSoldConfirmation = null }) {
                            Text(stringResource(R.string.sell_ok), color = Amber)
                        }
                    }
                }
            }

            // Employee footer with settings + logout
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateToSettings, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            contentDescription = "Settings",
                            modifier = Modifier.size(20.dp),
                            tint = OnSurfaceVariant
                        )
                    }
                    Text(
                        "Signed in as $employeeName",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = onLogout,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = "Log out",
                            modifier = Modifier.size(16.dp),
                            tint = Clay
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.sell_logout), color = Clay, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
    }
}
