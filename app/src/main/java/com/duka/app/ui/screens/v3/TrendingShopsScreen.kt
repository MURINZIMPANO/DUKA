package com.duka.app.ui.screens.v3

/**
 * V3 Trending Shops Screen — cross-district ranked business list.
 *
 * SCOPE-GUARD: READS from V1/V2 Business and V3 ShopRating tables (read-only).
 * No writes. Public-profile-only data — no private sales figures shown.
 *
 * MOCK STATUS: Rankings are from locally-seeded ShopRating data.
 * District filters are local. No real geo-fencing or analytics.
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
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.duka.app.data.local.entity.Business
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ShopRatingRepository
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
fun TrendingShopsScreen(
    businessRepository: BusinessRepository,
    shopRatingRepository: ShopRatingRepository,
    onBack: (() -> Unit)? = null
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val allRatings by shopRatingRepository.getAllRanked().collectAsState(initial = emptyList())

    var selectedDistrict by remember { mutableStateOf("All") }
    val districts = listOf("All", "Gasabo", "Nyarugenge", "Kicukiro")

    // For demo, we only have one business — show it as trending
    val trendingBusinesses = remember(business, allRatings) {
        if (business != null) {
            listOf(
                TrendingBusiness(
                    name = business!!.name,
                    district = business!!.district,
                    type = business!!.type,
                    rating = allRatings.firstOrNull { it.businessId == business!!.id }?.averageRating ?: 4.2,
                    ratingCount = allRatings.firstOrNull { it.businessId == business!!.id }?.ratingCount ?: 15,
                    summary = "Consistent sales and strong customer satisfaction"
                )
            )
        } else emptyList()
    }

    val filteredBusinesses = if (selectedDistrict == "All") {
        trendingBusinesses
    } else {
        trendingBusinesses.filter { it.district == selectedDistrict }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.TRENDING,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.trending_title), color = White) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // District filter chips
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    districts.forEach { district ->
                        FilterChip(
                            selected = selectedDistrict == district,
                            onClick = { selectedDistrict = district },
                            label = { Text(district) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Forest,
                                selectedLabelColor = White,
                                containerColor = White,
                                labelColor = OnSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Ranked list
            itemsIndexed(filteredBusinesses) { index, biz ->
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rank number
                            Surface(
                                modifier = Modifier.size(32.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = Forest
                            ) {
                                androidx.compose.foundation.layout.Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${index + 1}",
                                        color = White,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    biz.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "${biz.district} \u2022 ${biz.type}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                )
                                Text(
                                    biz.summary,
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
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
                                        String.format("%.1f", biz.rating),
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

private data class TrendingBusiness(
    val name: String,
    val district: String,
    val type: String,
    val rating: Double,
    val ratingCount: Int,
    val summary: String
)
