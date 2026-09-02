package com.duka.app.ui.screens.v4

/**
 * V4 Wholesaler Marketplace Screen — search and browse wholesalers.
 *
 * SCOPE-GUARD: READS from V4 Wholesaler table only. No V1/V2 table access.
 * No writes to any table.
 *
 * MOCK STATUS: Wholesaler data is locally-seeded. Delivery estimates are mock.
 * No real wholesaler partnerships or supply chain integration.
 */

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.repository.WholesalerRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R

@Composable
fun WholesalerMarketplaceScreen(
    wholesalerRepository: WholesalerRepository,
    onBack: (() -> Unit)? = null
) {
    var searchQuery by remember { mutableStateOf("") }

    val wholesalers by if (searchQuery.isBlank()) {
        wholesalerRepository.getAll()
    } else {
        wholesalerRepository.search(searchQuery)
    }.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.WHOLESALE,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.wholesaler_title), color = White) },
                navigationIcon = {
                    onBack?.let { back ->
                        androidx.compose.material3.IconButton(onClick = back) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
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
                placeholder = { Text(stringResource(R.string.wholesaler_search_hint)) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
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

            // Wholesaler list
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (wholesalers.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.Storefront,
                                contentDescription = "No wholesalers",
                                modifier = Modifier.size(48.dp),
                                tint = OnSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No wholesalers found", style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant))
                        }
                    }
                }

                itemsIndexed(wholesalers) { index, wholesaler ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay((index * 40).toLong())
                        visible = true
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(300)) + slideInVertically(tween(300, easing = FastOutSlowInEasing))
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        wholesaler.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        wholesaler.specialty,
                                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                    )
                                    Text(
                                        "Delivery: ${wholesaler.deliveryEstimate}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Forest)
                                    )
                                }
                                // Rating chip
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Amber.copy(alpha = 0.1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.Star,
                                            contentDescription = "Rating",
                                            modifier = Modifier.size(14.dp),
                                            tint = Amber
                                        )
                                        Text(
                                            String.format("%.1f", wholesaler.rating),
                                            style = MaterialTheme.typography.labelSmall.copy(color = Amber)
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
