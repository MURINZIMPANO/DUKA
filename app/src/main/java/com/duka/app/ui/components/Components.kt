package com.duka.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White

/**
 * Modifier that adds press-scale animation to any composable.
 * Scale down to ~0.97 with a spring on press for tactile feedback.
 */
fun Modifier.pressScale(): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pressScale"
    )
    this.scale(scale).then(
        Modifier /* interactionSource is passed through if needed */
    )
}

/**
 * Primary button: Forest fill, white text, press-scale animation.
 */
@Composable
fun DukaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "btnScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Forest,
            contentColor = White,
            disabledContainerColor = Mist,
            disabledContentColor = OnSurfaceVariant
        )
    ) {
        Text(
            text,
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/**
 * Secondary/accent button: Amber fill, Ink text, press-scale animation.
 */
@Composable
fun DukaSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "btnScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Amber,
            contentColor = Ink,
            disabledContainerColor = Mist,
            disabledContentColor = OnSurfaceVariant
        )
    ) {
        Text(
            text,
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/**
 * Tertiary/ghost button: Mist border, Ink text, transparent fill.
 */
@Composable
fun DukaTertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "btnScale"
    )

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        interactionSource = interactionSource
    ) {
        Text(
            text,
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Ink
        )
    }
}

/**
 * Status chip: small pill shape with colored border and text.
 */
@Composable
fun StatusChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = color.copy(alpha = 0.1f),
            modifier = Modifier
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )
        }
    }
}

/**
 * Shimmer loading placeholder using animated linear gradient.
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        Mist.copy(alpha = 0.6f),
        White.copy(alpha = 0.6f),
        Mist.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerTranslate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Box(
        modifier = modifier
            .background(brush)
    )
}
