package com.duka.phase3.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant

/**
 * Phase 3 — Optional filter row for "Browse all shops".
 * Location, rating, and rough price-cap filters. Everything starts empty/zero:
 * the base Explore state must show every shop (spec requirement).
 */
@Composable
internal fun ExploreFilterRow(
    filterDistrict: String,
    onDistrictChange: (String) -> Unit,
    minRating: Double,
    onMinRatingChange: (Double) -> Unit,
    maxPrice: Double,
    onMaxPriceChange: (Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // District filter — single-select chips, "All districts" clears.
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("" to "All districts", "Kigali" to "Kigali", "Musanze" to "Musanze", "Huye" to "Huye", "Rubavu" to "Rubavu")
                .forEach { (value, label) ->
                    FilterChip(
                        selected = filterDistrict == value,
                        onClick = { onDistrictChange(value) },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Forest,
                            selectedLabelColor = com.duka.app.ui.theme.White,
                            containerColor = com.duka.app.ui.theme.White,
                            labelColor = Ink.copy(alpha = 0.8f)
                        )
                    )
                }
        }
        // Rating filter.
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(0.0 to "Any rating", 3.0 to "3★+", 4.0 to "4★+", 4.5 to "4.5★+")
                .forEach { (value, label) ->
                    FilterChip(
                        selected = minRating == value,
                        onClick = { onMinRatingChange(value) },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Forest,
                            selectedLabelColor = com.duka.app.ui.theme.White,
                            containerColor = com.duka.app.ui.theme.White,
                            labelColor = Ink.copy(alpha = 0.8f)
                        )
                    )
                }
        }
        // Rough price cap (average product price).
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(0.0 to "Any price", 1000.0 to "Under ₣1k", 5000.0 to "Under ₣5k")
                .forEach { (value, label) ->
                    FilterChip(
                        selected = maxPrice == value,
                        onClick = { onMaxPriceChange(value) },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Forest,
                            selectedLabelColor = com.duka.app.ui.theme.White,
                            containerColor = com.duka.app.ui.theme.White,
                            labelColor = Ink.copy(alpha = 0.8f)
                        )
                    )
                }
        }
        Text(
            "Filters are optional — clear them to see every shop.",
            fontSize = 11.sp,
            color = OnSurfaceVariant
        )
    }
}
