package com.duka.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DukaColorScheme = lightColorScheme(
    primary = Forest,
    onPrimary = White,
    primaryContainer = ForestLight,
    onPrimaryContainer = White,
    secondary = Amber,
    onSecondary = Ink,
    secondaryContainer = Color(0xFFFFF3D6),
    onSecondaryContainer = Ink,
    tertiary = Clay,
    onTertiary = White,
    tertiaryContainer = Color(0xFFF5DAD4),
    onTertiaryContainer = Ink,
    background = Canvas,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Color(0xFFC8C3B5),
    error = Clay,
    onError = White
)

private val DukaDarkColorScheme = darkColorScheme(
    primary = Forest,
    onPrimary = White,
    primaryContainer = ForestLight,
    onPrimaryContainer = White,
    secondary = Amber,
    onSecondary = Ink,
    secondaryContainer = Color(0xFF3D3018),
    onSecondaryContainer = Amber,
    tertiary = Clay,
    onTertiary = White,
    tertiaryContainer = Color(0xFF3D1A14),
    onTertiaryContainer = Color(0xFFFFDAD4),
    // Dark mode: Canvas → Ink (#1C2321), Ink → Canvas (#F3EFE6)
    background = Ink,    // #1C2321
    onBackground = Canvas, // #F3EFE6
    surface = Color(0xFF242B29), // slightly lighter than Ink for cards
    onSurface = Canvas,   // #F3EFE6
    surfaceVariant = Color(0xFF2E3633),
    onSurfaceVariant = Color(0xFFB0ADA3),
    outline = Color(0xFF4A4A42),
    error = Clay,
    onError = White
)

@Composable
fun DukaTheme(content: @Composable () -> Unit) {
    // Respect the system/forced dark mode set by AppCompatDelegate
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) DukaDarkColorScheme else DukaColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Forest.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DukaTypography,
        content = content
    )
}
