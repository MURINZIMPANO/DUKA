package com.duka.analytics.charts

/**
 * DukaPieChart — Custom Compose Canvas donut pie chart.
 *
 * Used for both Owner (revenue breakdown) and Client (spending breakdown).
 * No external charting library — pure Canvas drawing.
 */

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.ForestLight
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import java.text.NumberFormat
import java.util.Locale

data class PieSlice(
    val label: String,
    val value: Float,
    val color: Color
)

/**
 * Fixed palette for category-based slices — consistent across time periods.
 * Unmapped categories get a deterministic color from hashCode().
 */
val sliceColorPalette = listOf(
    Forest, Amber, ForestLight, Clay, Mist,
    Color(0xFF5C6BC0), Color(0xFF26A69A), Color(0xFFEF5350)
)

fun colorForSlice(label: String): Color {
    return when (label) {
        "Grocery" -> Forest
        "Bakery" -> Amber
        "Pharmacy" -> ForestLight
        "Supermarket" -> Mist
        "Other" -> Clay
        else -> sliceColorPalette[kotlin.math.abs(label.hashCode()) % sliceColorPalette.size]
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DukaPieChart(
    slices: List<PieSlice>,
    modifier: Modifier = Modifier,
    animateOnAppear: Boolean = true,
    centerLabel: String? = null,
    centerValue: String? = null
) {
    var selectedSliceIndex by remember { mutableIntStateOf(-1) }
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    // Animation multiplier 0f→1f on appear
    var animationTriggered by remember { mutableStateOf(false) }
    LaunchedEffect(slices) {
        animationTriggered = false
        animationTriggered = true
    }
    val animationProgress by animateFloatAsState(
        targetValue = if (animationTriggered && animateOnAppear) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "pieAnimation"
    )

    val totalValue = slices.sumOf { it.value.toDouble() }.toFloat()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (slices.isEmpty() || totalValue == 0f) {
            // Empty state — dashed circle
            Canvas(
                modifier = Modifier
                    .size(180.dp)
                    .padding(16.dp)
            ) {
                drawArc(
                    color = Mist,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                    )
                )
            }
            Text(
                "No data for this period.",
                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            // Donut chart
            Box(contentAlignment = Alignment.Center) {
                Canvas(
                    modifier = Modifier
                        .size(180.dp)
                        .padding(16.dp)
                ) {
                    val outerRadius = size.minDimension / 2f
                    val innerRadius = outerRadius * 0.55f
                    val strokeWidth = outerRadius - innerRadius
                    val topLeft = Offset(
                        (size.width - outerRadius * 2) / 2f,
                        (size.height - outerRadius * 2) / 2f
                    )
                    val arcSize = Size(outerRadius * 2, outerRadius * 2)

                    // Draw slices
                    var startAngle = -90f
                    val totalSlices = slices.size
                    val gapAngle = if (totalSlices > 1) 2f else 0f

                    slices.forEachIndexed { index, slice ->
                        val sweepAngle = ((slice.value / totalValue) * 360f * animationProgress) - gapAngle
                        val isSelected = index == selectedSliceIndex
                        val radius = if (isSelected) outerRadius + 4.dp.toPx() else outerRadius
                        val isSelectedArc = isSelected
                        val arcStyle = Stroke(width = strokeWidth)

                        if (sweepAngle > 0f) {
                            if (isSelectedArc) {
                                // Draw selected slice slightly outward
                                val selectedArcSize = Size(radius * 2, radius * 2)
                                val selectedTopLeft = Offset(
                                    (size.width - radius * 2) / 2f,
                                    (size.height - radius * 2) / 2f
                                )
                                drawArc(
                                    color = slice.color,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = selectedTopLeft,
                                    size = selectedArcSize,
                                    style = arcStyle
                                )
                            } else {
                                drawArc(
                                    color = slice.color,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = arcStyle
                                )
                            }
                        }
                        startAngle += (slice.value / totalValue) * 360f * animationProgress
                    }
                }

                // Center label + value
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (centerLabel != null) {
                        Text(
                            centerLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                color = Ink.copy(alpha = 0.6f)
                            )
                        )
                    }
                    if (centerValue != null) {
                        Text(
                            centerValue,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Ink
                            )
                        )
                    }
                }
            }

            // Tap targets (invisible clickable overlay for each slice)
            // We use a simplified approach: the Canvas itself doesn't handle taps easily,
            // so we add a note about selection via the legend items below.

            // Selected slice tooltip
            if (selectedSliceIndex >= 0 && selectedSliceIndex < slices.size) {
                val selected = slices[selectedSliceIndex]
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Mist,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(selected.color, RoundedCornerShape(2.dp))
                        )
                        Text(
                            selected.label,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            "${currencyFormat.format(selected.value.toLong())} RWF",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                color = Forest
                            ),
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        val percentage = if (totalValue > 0) (selected.value / totalValue * 100) else 0f
                        Text(
                            "${String.format("%.0f", percentage)}%",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            // Legend — tappable to select slice
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                slices.forEachIndexed { index, slice ->
                    val percentage = if (totalValue > 0) (slice.value / totalValue * 100) else 0f
                    Row(
                        modifier = Modifier
                            .clickable {
                                selectedSliceIndex = if (selectedSliceIndex == index) -1 else index
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(slice.color, RoundedCornerShape(2.dp))
                        )
                        Text(
                            slice.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp,
                                color = Ink
                            ),
                            maxLines = 1
                        )
                        Text(
                            "${currencyFormat.format(slice.value.toLong())} · ${String.format("%.0f", percentage)}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Ink.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    }
}
