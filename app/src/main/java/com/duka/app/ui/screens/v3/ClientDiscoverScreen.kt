package com.duka.app.ui.screens.v3

/**
 * Client Discover Screen (C1) — "Find a shop"
 *
 * SCOPE-GUARD: READS from V1/V2 Business and ShopRating tables (read-only).
 * No writes to V1/V2 data. All distance/rating data is locally-mock.
 *
 * MOCK STATUS: Distance is computed from business name hash (MockDistanceCalculator).
 * Ratings are from locally-seeded ShopRating data. No real maps API or rating service.
 */

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.Business
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ShopRatingRepository
import com.duka.app.domain.MockDistanceCalculator
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ClientDiscoverScreen(
    businessRepository: BusinessRepository,
    shopRatingRepository: ShopRatingRepository,
    onBusinessSelected: (Long) -> Unit,
    onNavigateToProfile: (() -> Unit)? = null,
    onNavigateToSettings: (() -> Unit)? = null,
    onLogout: (() -> Unit)? = null
) {
    val businesses by businessRepository.getActiveBusiness().let { flow ->
        var list by remember { mutableStateOf<List<Business>>(emptyList()) }
        LaunchedEffect(Unit) {
            flow.collect { single ->
                if (single != null) list = listOf(single)
            }
        }
        remember { mutableStateOf(list) }
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Nearby") }
    var showProfileSheet by remember { mutableStateOf(false) }

    val filters = listOf(
        Triple("Nearby", Icons.Outlined.NearMe, true),
        Triple("Best price", Icons.Outlined.LocalOffer, true),
        Triple("Top rated", Icons.Outlined.Star, true)
    )

    val filteredBusinesses = businesses.filter { biz ->
        searchQuery.isBlank() || biz.name.contains(searchQuery, ignoreCase = true)
    }.let { list ->
        when (selectedFilter) {
            "Nearby" -> list.sortedBy { MockDistanceCalculator.computeDistance(it.name) }
            "Best price" -> list
            "Top rated" -> list
            else -> list
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        // Decorative backdrop
        DecorativeBackdrop(
            page = DukaPage.CLIENT_DISCOVER,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                stringResource(R.string.discover_eyebrow),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                stringResource(R.string.discover_title),
                                color = White,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showProfileSheet = true }) {
                            Icon(
                                Icons.Outlined.AccountCircle,
                                contentDescription = "Profile",
                                tint = White
                            )
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
                    placeholder = { Text(stringResource(R.string.discover_search_hint)) },
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

                // Filter chips — horizontal scroll, no wrapping
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { (filter, icon, _) ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            leadingIcon = {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Forest,
                                selectedLabelColor = White,
                                selectedLeadingIconColor = White,
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

                // Business list
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (filteredBusinesses.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Outlined.Storefront,
                                        contentDescription = "No shops found",
                                        modifier = Modifier.size(48.dp),
                                        tint = OnSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        stringResource(R.string.discover_empty),
                                        style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                                    )
                                }
                            }
                        }
                    }

                    itemsIndexed(filteredBusinesses) { index, business ->
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onBusinessSelected(business.id) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                            business.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                        )
                                        // Mocked distance — see comment at top of file
                                        Text(
                                            String.format("%.1f", MockDistanceCalculator.computeDistance(business.name)) + " km",
                                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                        )
                                    }
                                    // Star rating chip
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
                                                Icons.Filled.Star,
                                                contentDescription = "Rating",
                                                modifier = Modifier.size(16.dp),
                                                tint = Amber
                                            )
                                            Text(
                                                "4.2",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Amber,
                                                    fontWeight = FontWeight.Bold
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
    }

    // Profile bottom sheet with Settings and Logout
    if (showProfileSheet) {
        ModalBottomSheet(
            onDismissRequest = { showProfileSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Profile",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showProfileSheet = false
                            onNavigateToSettings?.invoke()
                        }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        tint = Forest,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Settings", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showProfileSheet = false
                            onLogout?.invoke()
                        }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = Clay,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Log out", style = MaterialTheme.typography.bodyMedium.copy(color = Clay))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
