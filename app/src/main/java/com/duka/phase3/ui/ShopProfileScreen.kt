package com.duka.phase3.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White

/**
 * Phase 3 — Shop Profile, opened from an Explore card.
 * Cover banner, identity block, Message button (opens Owner↔Client chat),
 * and a clean product grid scoped to this shop. Data comes from the local
 * cache (offline-safe); a best-effort refresh pulls the live catalog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopProfileScreen(
    repository: com.duka.phase3.data.Phase3Repository,
    shopRemoteId: String,
    onBack: () -> Unit,
    onMessageShop: (shopRemoteId: String, shopName: String) -> Unit
) {
    val shop by repository.observeShop(shopRemoteId).collectAsState(initial = null)
    val products by repository.observeShopProducts(shopRemoteId).collectAsState(initial = emptyList())

    // Best-effort catalog refresh; cached rows still render if offline.
    LaunchedEffect(shopRemoteId) {
        repository.refreshShopProducts(shopRemoteId)
    }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            TopAppBar(
                title = { Text(shop?.name ?: "Shop", color = White, maxLines = 1, overflow = TextOverflow.Ellipsis) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Cover banner — clean placeholder (no fake photography).
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(categoryAccent(shop?.category ?: "").copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        categoryIcon(shop?.category ?: ""),
                        contentDescription = null,
                        tint = categoryAccent(shop?.category ?: ""),
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        shop?.name ?: "Unknown shop",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CategoryBadge(shop?.category ?: "")
                        Spacer(Modifier.width(10.dp))
                        if ((shop?.ratingCount ?: 0) > 0) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = Amber, modifier = Modifier.size(15.dp))
                            Text(
                                " %.1f (%d)".format(shop?.ratingAverage ?: 0.0, shop?.ratingCount ?: 0),
                                fontSize = 13.sp, color = Ink
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        Text(shop?.district ?: "", fontSize = 13.sp, color = OnSurfaceVariant)
                    }
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = { onMessageShop(shopRemoteId, shop?.name ?: "Shop") },
                        colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Message")
                    }
                }
            }

            item {
                Text(
                    "Products",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            }

            if (products.isEmpty()) {
                item {
                    Text(
                        "No products published yet.",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = OnSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                items(products, key = { it.remoteId }) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(categoryAccent(product.category).copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    categoryIcon(product.category),
                                    contentDescription = null,
                                    tint = categoryAccent(product.category),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink)
                                if (product.category.isNotBlank()) {
                                    Text(product.category, fontSize = 11.sp, color = OnSurfaceVariant)
                                }
                            }
                            Text(
                                "₣%,.0f".format(product.price),
                                fontWeight = FontWeight.Bold,
                                color = Forest,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
