package com.duka.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import kotlin.random.Random

/**
 * DukaPage — identifies each screen for its unique decorative motif set.
 * Each page has an associated seed for deterministic placement.
 */
enum class DukaPage(val seed: Int) {
    AUTH(seed = 1),
    DASHBOARD(seed = 2),
    ADD_PRODUCT(seed = 3),
    SELL(seed = 4),
    VOICE_ADD(seed = 5),
    TAX_EBM(seed = 6),
    CHAT(seed = 7),
    PROMO(seed = 8),
    REPORT_ISSUE(seed = 9),
    CLIENT_DISCOVER(seed = 10),
    CLIENT_STORE(seed = 11),
    CLIENT_EBM(seed = 12),
    CLIENT_BUDGET(seed = 13),
    CLIENT_FEEDBACK(seed = 14),
    CREDIT(seed = 15),
    TRENDING(seed = 16),
    RESTOCK(seed = 17),
    WHOLESALE(seed = 18),
    SETTINGS(seed = 19)
}

/**
 * DecorativeBackdrop — renders subtle, per-page line-art motifs behind screen content.
 *
 * RULES:
 * - Single composable, one `when` branch per page — never copy-pasted per screen.
 * - All motifs: vector paths drawn on a Canvas, single muted tone, 7–9% opacity.
 * - Never overlapping body content: backdrop sits at fillMaxSize behind a Box.
 * - No animation, no motion — it is texture only.
 * - Motif sizes: 22–38dp, irregularly placed, rotated ±5–15° off-axis.
 * - Deterministic placement via Random(seed).
 */
@Composable
fun DecorativeBackdrop(
    page: DukaPage,
    modifier: Modifier = Modifier
) {
    val placements = remember(page) {
        generatePlacements(page.seed, count = 8)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            placements.forEach { placement ->
                translate(
                    left = placement.x * canvasWidth,
                    top = placement.y * canvasHeight
                ) {
                    rotate(degrees = placement.rotation, pivot = Offset.Zero) {
                        when (page) {
                            DukaPage.AUTH -> drawAuthMotif(placement.motif, this)
                            DukaPage.DASHBOARD -> drawDashboardMotif(placement.motif, this)
                            DukaPage.ADD_PRODUCT -> drawAddProductMotif(placement.motif, this)
                            DukaPage.SELL -> drawSellMotif(placement.motif, this)
                            DukaPage.VOICE_ADD -> drawVoiceAddMotif(placement.motif, this)
                            DukaPage.TAX_EBM -> drawTaxEbmMotif(placement.motif, this)
                            DukaPage.CHAT -> drawChatMotif(placement.motif, this)
                            DukaPage.PROMO -> drawPromoMotif(placement.motif, this)
                            DukaPage.REPORT_ISSUE -> drawReportIssueMotif(placement.motif, this)
                            DukaPage.CLIENT_DISCOVER -> drawClientDiscoverMotif(placement.motif, this)
                            DukaPage.CLIENT_STORE -> drawClientStoreMotif(placement.motif, this)
                            DukaPage.CLIENT_EBM -> drawClientEbmMotif(placement.motif, this)
                            DukaPage.CLIENT_BUDGET -> drawClientBudgetMotif(placement.motif, this)
                            DukaPage.CLIENT_FEEDBACK -> drawClientFeedbackMotif(placement.motif, this)
                            DukaPage.CREDIT -> drawCreditMotif(placement.motif, this)
                            DukaPage.TRENDING -> drawTrendingMotif(placement.motif, this)
                            DukaPage.RESTOCK -> drawRestockMotif(placement.motif, this)
                            DukaPage.WHOLESALE -> drawWholesaleMotif(placement.motif, this)
                            DukaPage.SETTINGS -> drawSettingsMotif(placement.motif, this)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Backward-compatible DecorativeBackdrop — used by existing screens that haven't
 * been migrated to the DukaPage enum yet. Renders a generic motif set.
 * @deprecated Use DecorativeBackdrop(page: DukaPage) instead.
 */
@Composable
fun DecorativeBackdrop(
    density: DecorativeDensity = DecorativeDensity.Low,
    seed: Int = 0,
    modifier: Modifier = Modifier
) {
    val placements = remember(seed, density) {
        val count = when (density) {
            DecorativeDensity.Low -> 6
            DecorativeDensity.Medium -> 10
        }
        generatePlacements(seed, count)
    }

    val mistColor = remember(seed) { Mist.copy(alpha = 0.08f) }
    val forestColor = remember(seed) { Forest.copy(alpha = 0.06f) }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            placements.forEach { placement ->
                val color = if (placement.motif % 2 == 0) forestColor else mistColor
                val sw = 1.5.dp.toPx()

                translate(
                    left = placement.x * canvasWidth,
                    top = placement.y * canvasHeight
                ) {
                    rotate(degrees = placement.rotation, pivot = Offset.Zero) {
                        when (placement.motif % 6) {
                            0 -> drawPriceTagMotif(color, sw, this@Canvas)
                            1 -> drawCoinStackMotif(color, sw, this@Canvas)
                            2 -> drawReceiptMotif(color, sw, this@Canvas)
                            3 -> drawStorefrontMotif(color, sw, this@Canvas)
                            4 -> drawBasketMotif(color, sw, this@Canvas)
                            5 -> drawTagMotif(color, sw, this@Canvas)
                        }
                    }
                }
            }
        }
    }
}

enum class DecorativeDensity { Low, Medium }

// --- Placement generation ---

private data class MotifPlacement(
    val x: Float,
    val y: Float,
    val rotation: Float,
    val motif: Int
)

private fun generatePlacements(seed: Int, count: Int): List<MotifPlacement> {
    val rng = Random(seed)

    // Top area (0..0.3) — sparse, behind header/logo
    val topCount = count / 2
    val topPlacements = (0 until topCount).map {
        MotifPlacement(
            x = 0.05f + rng.nextFloat() * 0.9f,
            y = 0.02f + rng.nextFloat() * 0.28f,
            rotation = -8f + rng.nextFloat() * 16f,
            motif = rng.nextInt(6)
        )
    }

    // Bottom area (0.65..0.95) — below content
    val bottomPlacements = (0 until (count - topCount)).map {
        MotifPlacement(
            x = 0.05f + rng.nextFloat() * 0.9f,
            y = 0.65f + rng.nextFloat() * 0.3f,
            rotation = -10f + rng.nextFloat() * 20f,
            motif = rng.nextInt(6)
        )
    }

    return topPlacements + bottomPlacements
}

// =========================================================================
// Per-page motif drawing functions
// Each returns a color + strokeWidth for the Canvas to draw.
// =========================================================================

// --- AUTH: price tag, coin stack, receipt, brand triangle ---
private fun drawAuthMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.08f)
    when (motif % 4) {
        0 -> drawPriceTagMotif(c, sw, scope)
        1 -> drawCoinStackMotif(c, sw, scope)
        2 -> drawReceiptMotif(c, sw, scope)
        3 -> drawBrandTriangleMotif(c, sw, scope)
    }
    }
}

// --- DASHBOARD: bar chart, coin, receipt strip, lightning bolt ---
private fun drawDashboardMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.07f)
    when (motif % 4) {
        0 -> drawBarChartMotif(c, sw, scope)
        1 -> drawCoinMotif(c, sw, scope)
        2 -> drawReceiptMotif(c, sw, scope)
        3 -> drawLightningMotif(c, sw, scope)
    }
    }
}

// --- ADD_PRODUCT: crate/box, price tag, scales/balance ---
private fun drawAddProductMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.08f)
    when (motif % 3) {
        0 -> drawCrateMotif(c, sw, scope)
        1 -> drawPriceTagMotif(c, sw, scope)
        2 -> drawScalesMotif(c, sw, scope)
    }
    }
}

// --- SELL: basket, coin, barcode ---
private fun drawSellMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Ink.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawBasketMotif(c, sw, scope)
        1 -> drawCoinMotif(c, sw, scope)
        2 -> drawBarcodeMotif(c, sw, scope)
    }
    }
}

// --- VOICE_ADD: mic, sound-wave arcs, price tag ---
private fun drawVoiceAddMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.07f)
    when (motif % 3) {
        0 -> drawMicMotif(c, sw, scope)
        1 -> drawSoundWaveMotif(c, sw, scope)
        2 -> drawPriceTagMotif(c, sw, scope)
    }
    }
}

// --- TAX_EBM: receipt long, RWF coin, checkmark circle ---
private fun drawTaxEbmMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Ink.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawReceiptMotif(c, sw, scope)
        1 -> drawCoinMotif(c, sw, scope)
        2 -> drawCheckCircleMotif(c, sw, scope)
    }
    }
}

// --- CHAT: two chat bubbles, dot ellipsis ---
private fun drawChatMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawChatBubbleMotif(c, sw, scope)
        1 -> drawChatBubbleSmallMotif(c, sw, scope)
        2 -> drawDotEllipsisMotif(c, sw, scope)
    }
    }
}

// --- PROMO: percentage sign, ribbon/tag, sparkle ---
private fun drawPromoMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Amber.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawPercentSignMotif(c, sw, scope)
        1 -> drawRibbonTagMotif(c, sw, scope)
        2 -> drawSparkleMotif(c, sw, scope)
    }
    }
}

// --- REPORT_ISSUE: flag, document page, warning triangle ---
private fun drawReportIssueMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Clay.copy(alpha = 0.07f)
    when (motif % 3) {
        0 -> drawFlagMotif(c, sw, scope)
        1 -> drawDocumentMotif(c, sw, scope)
        2 -> drawWarningTriangleMotif(c, sw, scope)
    }
    }
}

// --- CLIENT_DISCOVER: storefront, map pin, star ---
private fun drawClientDiscoverMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.07f)
    when (motif % 3) {
        0 -> drawStorefrontMotif(c, sw, scope)
        1 -> drawMapPinMotif(c, sw, scope)
        2 -> drawStarMotif(c, sw, scope)
    }
    }
}

// --- CLIENT_STORE: shopping bag, price tag, coin ---
private fun drawClientStoreMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.07f)
    when (motif % 3) {
        0 -> drawShoppingBagMotif(c, sw, scope)
        1 -> drawPriceTagMotif(c, sw, scope)
        2 -> drawCoinMotif(c, sw, scope)
    }
    }
}

// --- CLIENT_EBM: check circle, receipt strip, coin ---
private fun drawClientEbmMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawCheckCircleMotif(c, sw, scope)
        1 -> drawReceiptMotif(c, sw, scope)
        2 -> drawCoinMotif(c, sw, scope)
    }
    }
}

// --- CLIENT_BUDGET: wallet, bar chart, coin stack ---
private fun drawClientBudgetMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Amber.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawWalletMotif(c, sw, scope)
        1 -> drawBarChartMotif(c, sw, scope)
        2 -> drawCoinStackMotif(c, sw, scope)
    }
    }
}

// --- CLIENT_FEEDBACK: speech bubble, flag, pencil ---
private fun drawClientFeedbackMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Ink.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawChatBubbleMotif(c, sw, scope)
        1 -> drawFlagMotif(c, sw, scope)
        2 -> drawPencilMotif(c, sw, scope)
    }
    }
}

// --- CREDIT: bar chart, upward arrow, coin stack ---
private fun drawCreditMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.07f)
    when (motif % 3) {
        0 -> drawBarChartMotif(c, sw, scope)
        1 -> drawUpArrowMotif(c, sw, scope)
        2 -> drawCoinStackMotif(c, sw, scope)
    }
    }
}

// --- TRENDING: trophy, star, map pin ---
private fun drawTrendingMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Amber.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawTrophyMotif(c, sw, scope)
        1 -> drawStarMotif(c, sw, scope)
        2 -> drawMapPinMotif(c, sw, scope)
    }
    }
}

// --- RESTOCK: crate, arrow-down-into-box, scales ---
private fun drawRestockMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Forest.copy(alpha = 0.07f)
    when (motif % 3) {
        0 -> drawCrateMotif(c, sw, scope)
        1 -> drawArrowDownBoxMotif(c, sw, scope)
        2 -> drawScalesMotif(c, sw, scope)
    }
    }
}

// --- SETTINGS: gear-cog, toggle-switch, settings outline ---
private fun drawSettingsMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Ink.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawGearCogMotif(c, sw, scope)
        1 -> drawToggleSwitchMotif(c, sw, scope)
        2 -> drawSettingsOutlineMotif(c, sw, scope)
    }
    }
}

// --- WHOLESALE: storefront, handshake, box ---
private fun drawWholesaleMotif(motif: Int, scope: DrawScope) {
    with(scope) {
    val sw = 1.5.dp.toPx()
    val c = Ink.copy(alpha = 0.06f)
    when (motif % 3) {
        0 -> drawStorefrontMotif(c, sw, scope)
        1 -> drawHandshakeMotif(c, sw, scope)
        2 -> drawCrateMotif(c, sw, scope)
    }
    }
}

// =========================================================================
// Reusable motif drawing primitives
// =========================================================================

private fun drawPriceTagMotif(color: Color, sw: Float, scope: DrawScope? = null) {
    val ctx = scope ?: return
        with(ctx) {
    val s = 24.dp.toPx()
    val path = Path().apply {
        moveTo(s * 0.3f, 0f)
        lineTo(s, 0f)
        lineTo(s, s * 0.7f)
        lineTo(s * 0.3f, s * 0.7f)
        lineTo(0f, s * 0.35f)
        close()
    }
    ctx.drawPath(path, color = color, style = Stroke(width = sw))
    ctx.drawCircle(color = color, radius = s * 0.06f, center = Offset(s * 0.6f, s * 0.35f), style = Stroke(width = sw * 0.7f))
        }
}

private fun drawCoinStackMotif(color: Color, sw: Float, scope: DrawScope? = null) {
    val ctx = scope ?: return
        with(ctx) {
    val s = 24.dp.toPx()
    repeat(3) { i ->
        ctx.drawOval(
            color = color,
            topLeft = Offset(0f, i * s * 0.35f),
            size = Size(s, s * 0.4f),
            style = Stroke(width = sw)
        )
    }
        }
}

private fun drawReceiptMotif(color: Color, sw: Float, scope: DrawScope? = null) {
    val ctx = scope ?: return
        with(ctx) {
    val s = 30.dp.toPx()
    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(s, 0f)
        lineTo(s, s * 0.7f)
        var x = s
        val step = s / 6f
        repeat(6) {
            val peak = if (it % 2 == 0) s * 0.85f else s * 0.7f
            x -= step
            lineTo(x, peak)
        }
        close()
    }
    ctx.drawPath(path, color = color, style = Stroke(width = sw))
    repeat(3) { i ->
        val y = s * 0.15f + i * s * 0.15f
        ctx.drawLine(color = color, start = Offset(s * 0.15f, y), end = Offset(s * 0.85f, y), strokeWidth = sw * 0.7f)
    }
        }
}

private fun drawBasketMotif(color: Color, sw: Float, scope: DrawScope? = null) {
    val ctx = scope ?: return
        with(ctx) {
    val s = 26.dp.toPx()
    val path = Path().apply {
        moveTo(s * 0.15f, 0f)
        lineTo(s * 0.85f, 0f)
        lineTo(s, s * 0.7f)
        lineTo(0f, s * 0.7f)
        close()
    }
    ctx.drawPath(path, color = color, style = Stroke(width = sw))
    ctx.drawArc(
        color = color, startAngle = 180f, sweepAngle = 180f, useCenter = false,
        topLeft = Offset(s * 0.2f, -s * 0.4f), size = Size(s * 0.6f, s * 0.5f),
        style = Stroke(width = sw)
    )
        }
}

private fun drawStorefrontMotif(color: Color, sw: Float, scope: DrawScope? = null) {
    val ctx = scope ?: return
        with(ctx) {
    val s = 32.dp.toPx()
    val roofPath = Path().apply {
        moveTo(0f, s * 0.4f)
        lineTo(s * 0.5f, 0f)
        lineTo(s, s * 0.4f)
        close()
    }
    ctx.drawPath(roofPath, color = color, style = Stroke(width = sw))
    ctx.drawRect(color = color, topLeft = Offset(s * 0.1f, s * 0.4f), size = Size(s * 0.8f, s * 0.55f), style = Stroke(width = sw))
    ctx.drawRect(color = color, topLeft = Offset(s * 0.38f, s * 0.6f), size = Size(s * 0.24f, s * 0.35f), style = Stroke(width = sw * 0.7f))
        }
}

private fun drawTagMotif(color: Color, sw: Float, scope: DrawScope? = null) {
    val ctx = scope ?: return
        with(ctx) {
    val s = 22.dp.toPx()
    ctx.drawRect(color = color, topLeft = Offset(0f, 0f), size = Size(s, s * 0.6f), style = Stroke(width = sw))
    ctx.drawCircle(color = color, radius = s * 0.08f, center = Offset(s * 0.8f, s * 0.3f), style = Stroke(width = sw * 0.7f))
        }
}

// --- New motifs for per-page ---

private fun drawBrandTriangleMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 36.dp.toPx()
    val path = Path().apply {
        moveTo(s * 0.5f, 0f)
        lineTo(s, s * 0.8f)
        lineTo(0f, s * 0.8f)
        close()
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    }
}

private fun drawBarChartMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Three bars of different heights
    val barWidth = s * 0.2f
    val heights = listOf(0.4f, 0.7f, 0.55f)
    heights.forEachIndexed { i, h ->
        scope.drawRect(
            color = color,
            topLeft = Offset(i * (barWidth + s * 0.1f), s * (1f - h)),
            size = Size(barWidth, s * h),
            style = Stroke(width = sw)
        )
    }
    }
}

private fun drawCoinMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 22.dp.toPx()
    scope.drawOval(color = color, topLeft = Offset(0f, 0f), size = Size(s, s * 0.7f), style = Stroke(width = sw))
    // Inner circle
    scope.drawCircle(color = color, radius = s * 0.2f, center = Offset(s * 0.5f, s * 0.35f), style = Stroke(width = sw * 0.7f))
    }
}

private fun drawLightningMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 24.dp.toPx()
    val path = Path().apply {
        moveTo(s * 0.6f, 0f)
        lineTo(s * 0.2f, s * 0.45f)
        lineTo(s * 0.5f, s * 0.45f)
        lineTo(s * 0.35f, s)
        lineTo(s * 0.8f, s * 0.4f)
        lineTo(s * 0.5f, s * 0.4f)
        close()
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    }
}

private fun drawCrateMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    scope.drawRect(color = color, topLeft = Offset(0f, 0f), size = Size(s, s * 0.7f), style = Stroke(width = sw))
    // Cross lines
    scope.drawLine(color = color, start = Offset(0f, s * 0.35f), end = Offset(s, s * 0.35f), strokeWidth = sw * 0.7f)
    scope.drawLine(color = color, start = Offset(s * 0.5f, 0f), end = Offset(s * 0.5f, s * 0.7f), strokeWidth = sw * 0.7f)
    }
}

private fun drawScalesMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Vertical pole
    scope.drawLine(color = color, start = Offset(s * 0.5f, 0f), end = Offset(s * 0.5f, s * 0.7f), strokeWidth = sw)
    // Horizontal beam
    scope.drawLine(color = color, start = Offset(s * 0.1f, s * 0.2f), end = Offset(s * 0.9f, s * 0.2f), strokeWidth = sw)
    // Left pan
    scope.drawLine(color = color, start = Offset(s * 0.1f, s * 0.2f), end = Offset(s * 0.05f, s * 0.5f), strokeWidth = sw * 0.7f)
    scope.drawLine(color = color, start = Offset(s * 0.05f, s * 0.5f), end = Offset(s * 0.3f, s * 0.5f), strokeWidth = sw * 0.7f)
    // Right pan
    scope.drawLine(color = color, start = Offset(s * 0.9f, s * 0.2f), end = Offset(s * 0.7f, s * 0.5f), strokeWidth = sw * 0.7f)
    scope.drawLine(color = color, start = Offset(s * 0.7f, s * 0.5f), end = Offset(s * 0.95f, s * 0.5f), strokeWidth = sw * 0.7f)
    }
}

private fun drawBarcodeMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 24.dp.toPx()
    val barWidth = s * 0.06f
    val pattern = listOf(1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1)
    var x = 0f
    pattern.forEach { filled ->
        if (filled == 1) {
            scope.drawRect(
                color = color,
                topLeft = Offset(x, 0f),
                size = Size(barWidth, s * 0.7f),
                style = Stroke(width = sw)
            )
        }
        x += barWidth + s * 0.04f
    }
    }
}

private fun drawMicMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 26.dp.toPx()
    // Mic body (rounded rect approximation)
    scope.drawRect(color = color, topLeft = Offset(s * 0.3f, 0f), size = Size(s * 0.4f, s * 0.55f), style = Stroke(width = sw))
    // Arc around mic
    scope.drawArc(
        color = color, startAngle = 0f, sweepAngle = 180f, useCenter = false,
        topLeft = Offset(s * 0.1f, s * 0.1f), size = Size(s * 0.8f, s * 0.7f),
        style = Stroke(width = sw)
    )
    // Stand
    scope.drawLine(color = color, start = Offset(s * 0.5f, s * 0.8f), end = Offset(s * 0.5f, s), strokeWidth = sw)
    scope.drawLine(color = color, start = Offset(s * 0.3f, s), end = Offset(s * 0.7f, s), strokeWidth = sw)
    }
}

private fun drawSoundWaveMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // 2-3 concentric arcs
    repeat(3) { i ->
        val radius = s * (0.15f + i * 0.15f)
        scope.drawArc(
            color = color, startAngle = -45f, sweepAngle = 90f, useCenter = false,
            topLeft = Offset(s * 0.5f - radius, s * 0.5f - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = sw * 0.7f)
        )
    }
    }
}

private fun drawCheckCircleMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    scope.drawCircle(color = color, radius = s * 0.45f, center = Offset(s * 0.5f, s * 0.5f), style = Stroke(width = sw))
    // Checkmark
    val path = Path().apply {
        moveTo(s * 0.3f, s * 0.5f)
        lineTo(s * 0.45f, s * 0.65f)
        lineTo(s * 0.7f, s * 0.35f)
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    }
}

private fun drawChatBubbleMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 30.dp.toPx()
    // Main bubble
    scope.drawRoundRect(
        color = color,
        topLeft = Offset(0f, 0f),
        size = Size(s, s * 0.65f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.1f),
        style = Stroke(width = sw)
    )
    // Tail
    val tail = Path().apply {
        moveTo(s * 0.15f, s * 0.65f)
        lineTo(s * 0.05f, s * 0.85f)
        lineTo(s * 0.35f, s * 0.65f)
    }
    scope.drawPath(tail, color = color, style = Stroke(width = sw))
    }
}

private fun drawChatBubbleSmallMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 22.dp.toPx()
    scope.drawRoundRect(
        color = color,
        topLeft = Offset(0f, 0f),
        size = Size(s, s * 0.55f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.1f),
        style = Stroke(width = sw * 0.7f)
    )
    }
}

private fun drawDotEllipsisMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 24.dp.toPx()
    repeat(3) { i ->
        scope.drawCircle(
            color = color,
            radius = s * 0.06f,
            center = Offset(s * (0.25f + i * 0.25f), s * 0.4f),
            style = Stroke(width = sw * 0.7f)
        )
    }
    }
}

private fun drawPercentSignMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    scope.drawCircle(color = color, radius = s * 0.12f, center = Offset(s * 0.25f, s * 0.25f), style = Stroke(width = sw))
    scope.drawCircle(color = color, radius = s * 0.12f, center = Offset(s * 0.75f, s * 0.75f), style = Stroke(width = sw))
    scope.drawLine(color = color, start = Offset(s * 0.8f, s * 0.15f), end = Offset(s * 0.2f, s * 0.85f), strokeWidth = sw)
    }
}

private fun drawRibbonTagMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 26.dp.toPx()
    val path = Path().apply {
        moveTo(s * 0.2f, 0f)
        lineTo(s, 0f)
        lineTo(s, s * 0.7f)
        lineTo(s * 0.5f, s * 0.85f)
        lineTo(s * 0.2f, s * 0.7f)
        close()
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    }
}

private fun drawSparkleMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 22.dp.toPx()
    // 4-point star (not 5-point — less playful per spec)
    val path = Path().apply {
        moveTo(s * 0.5f, 0f)
        lineTo(s * 0.6f, s * 0.4f)
        lineTo(s, s * 0.5f)
        lineTo(s * 0.6f, s * 0.6f)
        lineTo(s * 0.5f, s)
        lineTo(s * 0.4f, s * 0.6f)
        lineTo(0f, s * 0.5f)
        lineTo(s * 0.4f, s * 0.4f)
        close()
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw * 0.7f))
    }
}

private fun drawFlagMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 26.dp.toPx()
    // Pole
    scope.drawLine(color = color, start = Offset(s * 0.2f, 0f), end = Offset(s * 0.2f, s), strokeWidth = sw)
    // Flag body
    val flagPath = Path().apply {
        moveTo(s * 0.2f, 0f)
        lineTo(s * 0.9f, s * 0.15f)
        lineTo(s * 0.2f, s * 0.35f)
    }
    scope.drawPath(flagPath, color = color, style = Stroke(width = sw))
    }
}

private fun drawDocumentMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    scope.drawRect(color = color, topLeft = Offset(0f, 0f), size = Size(s, s * 0.8f), style = Stroke(width = sw))
    // Text lines
    repeat(3) { i ->
        val y = s * 0.15f + i * s * 0.2f
        scope.drawLine(color = color, start = Offset(s * 0.15f, y), end = Offset(s * 0.85f, y), strokeWidth = sw * 0.5f)
    }
    }
}

private fun drawWarningTriangleMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    val path = Path().apply {
        moveTo(s * 0.5f, 0f)
        lineTo(s, s * 0.85f)
        lineTo(0f, s * 0.85f)
        close()
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    // Exclamation mark
    scope.drawLine(color = color, start = Offset(s * 0.5f, s * 0.3f), end = Offset(s * 0.5f, s * 0.55f), strokeWidth = sw)
    scope.drawCircle(color = color, radius = s * 0.03f, center = Offset(s * 0.5f, s * 0.7f))
    }
}

private fun drawMapPinMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Pin body
    scope.drawOval(color = color, topLeft = Offset(s * 0.1f, 0f), size = Size(s * 0.8f, s * 0.5f), style = Stroke(width = sw))
    // Pin point
    val pointPath = Path().apply {
        moveTo(s * 0.2f, s * 0.45f)
        lineTo(s * 0.5f, s)
        lineTo(s * 0.8f, s * 0.45f)
    }
    scope.drawPath(pointPath, color = color, style = Stroke(width = sw))
    }
}

private fun drawStarMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 26.dp.toPx()
    val cx = s * 0.5f
    val cy = s * 0.5f
    val outerR = s * 0.45f
    val innerR = s * 0.2f
    val path = Path()
    for (i in 0 until 5) {
        val angleOuter = Math.toRadians((-90 + i * 72).toDouble())
        val angleInner = Math.toRadians((-90 + i * 72 + 36).toDouble())
        val ox = cx + outerR * kotlin.math.cos(angleOuter).toFloat()
        val oy = cy + outerR * kotlin.math.sin(angleOuter).toFloat()
        val ix = cx + innerR * kotlin.math.cos(angleInner).toFloat()
        val iy = cy + innerR * kotlin.math.sin(angleInner).toFloat()
        if (i == 0) path.moveTo(ox, oy) else path.lineTo(ox, oy)
        path.lineTo(ix, iy)
    }
    path.close()
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    }
}

private fun drawShoppingBagMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Bag body
    scope.drawRect(color = color, topLeft = Offset(0f, s * 0.3f), size = Size(s, s * 0.7f), style = Stroke(width = sw))
    // Handle
    scope.drawArc(
        color = color, startAngle = 180f, sweepAngle = 180f, useCenter = false,
        topLeft = Offset(s * 0.2f, 0f), size = Size(s * 0.6f, s * 0.4f),
        style = Stroke(width = sw)
    )
    }
}

private fun drawWalletMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 30.dp.toPx()
    // Wallet body
    scope.drawRect(color = color, topLeft = Offset(0f, s * 0.1f), size = Size(s, s * 0.65f), style = Stroke(width = sw))
    // Flap
    scope.drawLine(color = color, start = Offset(0f, s * 0.1f), end = Offset(s * 0.65f, s * 0.1f), strokeWidth = sw)
    // Clasp
    scope.drawCircle(color = color, radius = s * 0.06f, center = Offset(s * 0.75f, s * 0.35f), style = Stroke(width = sw * 0.7f))
    }
}

private fun drawPencilMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Pencil body
    scope.drawRect(color = color, topLeft = Offset(s * 0.35f, 0f), size = Size(s * 0.3f, s * 0.7f), style = Stroke(width = sw))
    // Tip
    val tipPath = Path().apply {
        moveTo(s * 0.35f, s * 0.7f)
        lineTo(s * 0.5f, s)
        lineTo(s * 0.65f, s * 0.7f)
    }
    scope.drawPath(tipPath, color = color, style = Stroke(width = sw))
    }
}

private fun drawUpArrowMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 24.dp.toPx()
    val path = Path().apply {
        moveTo(s * 0.5f, 0f)
        lineTo(s * 0.8f, s * 0.35f)
        lineTo(s * 0.6f, s * 0.35f)
        lineTo(s * 0.6f, s)
        lineTo(s * 0.4f, s)
        lineTo(s * 0.4f, s * 0.35f)
        lineTo(s * 0.2f, s * 0.35f)
        close()
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    }
}

private fun drawTrophyMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Cup body
    scope.drawArc(
        color = color, startAngle = 0f, sweepAngle = 180f, useCenter = true,
        topLeft = Offset(s * 0.1f, 0f), size = Size(s * 0.8f, s * 0.6f),
        style = Stroke(width = sw)
    )
    // Base
    scope.drawRect(color = color, topLeft = Offset(s * 0.3f, s * 0.7f), size = Size(s * 0.4f, s * 0.1f), style = Stroke(width = sw * 0.7f))
    scope.drawLine(color = color, start = Offset(s * 0.5f, s * 0.6f), end = Offset(s * 0.5f, s * 0.7f), strokeWidth = sw * 0.7f)
    }
}

private fun drawArrowDownBoxMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Box
    scope.drawRect(color = color, topLeft = Offset(s * 0.1f, s * 0.4f), size = Size(s * 0.8f, s * 0.55f), style = Stroke(width = sw))
    // Arrow pointing down into box
    val arrowPath = Path().apply {
        moveTo(s * 0.5f, 0f)
        lineTo(s * 0.5f, s * 0.35f)
        moveTo(s * 0.3f, s * 0.2f)
        lineTo(s * 0.5f, s * 0.35f)
        lineTo(s * 0.7f, s * 0.2f)
    }
    scope.drawPath(arrowPath, color = color, style = Stroke(width = sw))
    }
}

private fun drawGearCogMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    val cx = s * 0.5f
    val cy = s * 0.5f
    // Outer circle
    scope.drawCircle(color = color, radius = s * 0.35f, center = Offset(cx, cy), style = Stroke(width = sw))
    // Inner circle
    scope.drawCircle(color = color, radius = s * 0.15f, center = Offset(cx, cy), style = Stroke(width = sw * 0.7f))
    // Teeth
    repeat(6) { i ->
        val angle = Math.toRadians((i * 60).toDouble())
        val outerR = s * 0.45f
        val innerR = s * 0.3f
        val ox = cx + outerR * kotlin.math.cos(angle).toFloat()
        val oy = cy + outerR * kotlin.math.sin(angle).toFloat()
        val ix = cx + innerR * kotlin.math.cos(angle).toFloat()
        val iy = cy + innerR * kotlin.math.sin(angle).toFloat()
        scope.drawLine(color = color, start = Offset(ix, iy), end = Offset(ox, oy), strokeWidth = sw * 0.7f)
    }
    }
}

private fun drawToggleSwitchMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Track
    scope.drawRoundRect(
        color = color,
        topLeft = Offset(0f, s * 0.25f),
        size = Size(s, s * 0.5f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.25f),
        style = Stroke(width = sw)
    )
    // Thumb
    scope.drawCircle(color = color, radius = s * 0.15f, center = Offset(s * 0.65f, s * 0.5f), style = Stroke(width = sw * 0.7f))
    }
}

private fun drawSettingsOutlineMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 28.dp.toPx()
    // Hexagon-ish outline (simplified gear)
    scope.drawRect(color = color, topLeft = Offset(s * 0.1f, s * 0.1f), size = Size(s * 0.8f, s * 0.8f), style = Stroke(width = sw))
    // Three horizontal lines (settings icon)
    repeat(3) { i ->
        val y = s * (0.3f + i * 0.2f)
        scope.drawLine(color = color, start = Offset(s * 0.2f, y), end = Offset(s * 0.8f, y), strokeWidth = sw * 0.7f)
    }
    }
}

private fun drawHandshakeMotif(color: Color, sw: Float, scope: DrawScope) {
    with(scope) {
    val s = 30.dp.toPx()
    // Simplified handshake: two angled arms meeting
    val path = Path().apply {
        moveTo(0f, s * 0.3f)
        lineTo(s * 0.35f, s * 0.5f)
        lineTo(s * 0.5f, s * 0.35f)
        lineTo(s * 0.65f, s * 0.5f)
        lineTo(s, s * 0.3f)
    }
    scope.drawPath(path, color = color, style = Stroke(width = sw))
    // Cuffs
    scope.drawLine(color = color, start = Offset(0f, s * 0.4f), end = Offset(s * 0.2f, s * 0.55f), strokeWidth = sw * 0.7f)
    scope.drawLine(color = color, start = Offset(s, s * 0.4f), end = Offset(s * 0.8f, s * 0.55f), strokeWidth = sw * 0.7f)
    }
}
