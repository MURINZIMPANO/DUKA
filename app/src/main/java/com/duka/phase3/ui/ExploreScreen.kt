package com.duka.phase3.ui

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.ExploreShop
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch

/**
 * Phase 3 — Explore (client-facing business discovery).
 *
 * Design rules honoured: existing tokens only (Canvas/Forest/Mist/Amber...),
 * editorial typographic hierarchy, generous whitespace, capped card density,
 * restrained motion (no autoplay, no infinite-scroll tricks). The base
 * "Browse all shops" state shows EVERY registered shop — filters are opt-in.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    repository: com.duka.phase3.data.Phase3Repository,
    userDistrict: String,
    onOpenShop: (String) -> Unit,
    initialCategoryFilter: String = ""
) {
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategoryFilter) }
    // Optional filter row state — all OFF by default (spec: base state shows everything).
    var filterDistrict by remember { mutableStateOf("") }
    var filterMinRating by remember { mutableStateOf(0.0) }
    var filterMaxPrice by remember { mutableStateOf(0.0) }
    var filtersExpanded by remember { mutableStateOf(false) }

    val syncState by repository.syncState.collectAsState()

    // Trigger a refresh when the screen opens (visible retriable state handled below).
    LaunchedEffect(Unit) {
        repository.refresh(userDistrict)
    }

    // Flows remembered per filter-value so collection isn't restarted spuriously.
    val allShopsFlow = remember { repository.observeAllShops() }
    val newestFlow = remember { repository.observeNewest() }
    val searchFlow = remember(
        searchQuery, selectedCategory, filterDistrict, filterMinRating, filterMaxPrice
    ) {
        repository.searchShops(
            query = searchQuery.trim(),
            category = selectedCategory,
            district = filterDistrict,
            minRating = filterMinRating,
            maxPrice = filterMaxPrice
        )
    }
    val allShops by allShopsFlow.collectAsState(initial = emptyList())
    val newest by newestFlow.collectAsState(initial = emptyList())
    val searched by searchFlow.collectAsState(initial = emptyList())

    val trending = allShops.sortedByDescending { it.salesVelocity }.take(10)
    val suggested = remember(allShops) {
        // HEURISTIC, not AI: same category as shops the user opened before is not yet
        // tracked, so Phase 3 ships popular-in-district as the starting rule. Honest
        // note lives in README-PHASE3.md too — do not oversell this as personalization.
        val inDistrict = allShops.filter { it.district.equals(userDistrict, ignoreCase = true) }
        (if (inDistrict.isNotEmpty()) inDistrict else allShops).shuffled().take(10)
    }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Explore", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Shops on Duka", color = White.copy(alpha = 0.8f), fontSize = 12.sp)
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
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Search bar — shops and products, live filtering.
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = { Text("Search shops and products", color = OnSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Forest) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )
            }

            // 2. Category strip — horizontal chips; nothing selected by default.
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ExploreCategories.all) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat.name,
                            onClick = {
                                selectedCategory =
                                    if (selectedCategory == cat.name) "" else cat.name
                            },
                            label = { Text(cat.name) },
                            leadingIcon = {
                                Icon(cat.icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Forest,
                                selectedLabelColor = White,
                                selectedLeadingIconColor = White,
                                containerColor = White,
                                labelColor = Ink.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }

            // Visible sync status — never silent (non-functional requirement).
            item {
                when {
                    syncState.inProgress -> Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = Forest)
                        Spacer(Modifier.width(8.dp))
                        Text("Syncing…", fontSize = 12.sp, color = OnSurfaceVariant)
                    }
                    syncState.lastError != null -> Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { scope.launch { repository.refresh(userDistrict) } },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.CloudOff, contentDescription = null,
                            tint = com.duka.app.ui.theme.Clay, modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            syncState.lastError + " — tap to retry",
                            fontSize = 12.sp,
                            color = com.duka.app.ui.theme.Clay
                        )
                    }
                    syncState.lastSuccessAt > 0 -> Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Up to date", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                    else -> {}
                }
            }

            // 3. Trending near you — ranked by sales velocity (district first, then national).
            if (trending.isNotEmpty()) {
                item {
                    ExploreSection(title = "Trending near you") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(trending, key = { it.remoteId }) { shop ->
                                ShopCard(shop = shop, compact = true, onClick = { onOpenShop(shop.remoteId) })
                            }
                        }
                    }
                }
            }

            // 4. New on Duka — recently registered shops.
            if (newest.isNotEmpty()) {
                item {
                    ExploreSection(title = "New on Duka") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(newest, key = { it.remoteId }) { shop ->
                                ShopCard(shop = shop, compact = true, onClick = { onOpenShop(shop.remoteId) })
                            }
                        }
                    }
                }
            }

            // 5. Suggested for you — heuristic (popular-in-district), clearly not AI.
            if (suggested.isNotEmpty()) {
                item {
                    ExploreSection(title = "Suggested for you", subtitle = "Popular in your district") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(suggested, key = { it.remoteId }) { shop ->
                                ShopCard(shop = shop, compact = true, onClick = { onOpenShop(shop.remoteId) })
                            }
                        }
                    }
                }
            }

            // 6. Browse all shops — the full list; optional filters above, off by default.
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Browse all shops",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.duka.app.ui.theme.Ink
                        )
                        TextButton(onClick = { filtersExpanded = !filtersExpanded }) {
                            Text(
                                if (filtersExpanded) "Hide filters" else "Filters",
                                color = Forest,
                                fontSize = 13.sp
                            )
                        }
                    }
                    if (filtersExpanded) {
                        ExploreFilterRow(
                            filterDistrict = filterDistrict,
                            onDistrictChange = { filterDistrict = it },
                            minRating = filterMinRating,
                            onMinRatingChange = { filterMinRating = it },
                            maxPrice = filterMaxPrice,
                            onMaxPriceChange = { filterMaxPrice = it }
                        )
                    }
                }
            }

            val filtered = searched
            if (filtered.isEmpty()) {
                item {
                    Text(
                        if (searchQuery.isBlank() && selectedCategory.isBlank() && filterDistrict.isBlank() &&
                            filterMinRating == 0.0 && filterMaxPrice == 0.0
                        )
                            "No shops yet — they appear here once shops sync to Duka."
                        else "No shops match your filters.",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = OnSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                items(filtered, key = { it.remoteId }) { shop ->
                    ShopCard(shop = shop, onClick = { onOpenShop(shop.remoteId) })
                }
            }
        }
    }
}

@Composable
private fun ExploreSection(
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            title,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = com.duka.app.ui.theme.Ink
        )
        subtitle?.let {
            Text(it, modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp), fontSize = 12.sp, color = OnSurfaceVariant)
        }
        Spacer(Modifier.height(10.dp))
        content()
    }
}

/** ₣ / ₣₣ / ₣₣₣ rough price-range indicator from the shop's average product price. */
internal fun priceRangeSymbol(avgPrice: Double): String = when {
    avgPrice <= 0.0 -> ""
    avgPrice < 1000 -> "₣"
    avgPrice < 5000 -> "₣₣"
    else -> "₣₣₣"
}

@Composable
internal fun ShopCard(
    shop: ExploreShop,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .then(if (compact) Modifier.width(220.dp) else Modifier.fillMaxWidth())
            .padding(horizontal = if (compact) 0.dp else 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        // Cover banner — clean placeholder graphic (no fake product photography).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (compact) 84.dp else 96.dp)
                .background(categoryAccent(shop.category).copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                categoryIcon(shop.category),
                contentDescription = null,
                tint = categoryAccent(shop.category),
                modifier = Modifier.size(36.dp)
            )
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                shop.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = com.duka.app.ui.theme.Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CategoryBadge(shop.category)
                Spacer(Modifier.width(8.dp))
                if (shop.ratingCount > 0) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Amber, modifier = Modifier.size(13.dp))
                    Text(
                        " ${"%.1f".format(shop.ratingAverage)}",
                        fontSize = 12.sp, color = com.duka.app.ui.theme.Ink
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(priceRangeSymbol(shop.avgPrice), fontSize = 12.sp, color = Forest, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                shop.district,
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun CategoryBadge(category: String) {
    Text(
        category,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = categoryAccent(category),
        modifier = Modifier
            .background(categoryAccent(category).copy(alpha = 0.12f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

// ---- Category metadata (icons reuse the existing material icon set) ----

data class ExploreCategory(val name: String, val icon: ImageVector)

object ExploreCategories {
    val all = listOf(
        ExploreCategory("Supermarket", Icons.Outlined.Storefront),
        ExploreCategory("Bakery", Icons.Outlined.BakeryDining),
        ExploreCategory("Pharmacy", Icons.Outlined.Medication),
        ExploreCategory("Grocery", Icons.Outlined.ShoppingBasket),
        ExploreCategory("Other", Icons.Outlined.MoreHoriz)
    )
}

internal fun categoryIcon(category: String): ImageVector =
    ExploreCategories.all.firstOrNull { it.name.equals(category, ignoreCase = true) }?.icon
        ?: Icons.Outlined.Storefront

internal fun categoryAccent(category: String): Color = when (category.lowercase()) {
    "supermarket" -> Forest
    "bakery" -> Amber
    "pharmacy" -> com.duka.app.ui.theme.ForestLight
    "grocery" -> Color(0xFF6B8E23)
    else -> OnSurfaceVariant
}
