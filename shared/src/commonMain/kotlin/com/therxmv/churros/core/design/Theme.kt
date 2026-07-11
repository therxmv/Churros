package com.therxmv.churros.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Extended color container ──────────────────────────────────────────────────

/**
 * Churros-specific color tokens that don't map directly to Material3 roles.
 * Access via [MaterialTheme.churrosColors].
 */
@Immutable
data class ChurrosColors(
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val divider: Color,
    val surfaceElevated: Color,
    // Semantic
    val success: Color,
    val info: Color,
    val event: Color,
    val warning: Color,
    val error: Color,
    // Sticky notes
    val stickyShoppingBackground: Color,
    val stickyChoresBackground: Color,
    val stickyReminderBackground: Color,
    val stickyEventBackground: Color,
    val stickyImportantBackground: Color,
)

val LocalChurrosColors = staticCompositionLocalOf<ChurrosColors> {
    error("No ChurrosColors provided — wrap your UI in ChurrosTheme")
}

val MaterialTheme.churrosColors: ChurrosColors
    @Composable get() = LocalChurrosColors.current

// ── Light color scheme ────────────────────────────────────────────────────────

private val LightColorScheme: ColorScheme = lightColorScheme(
    primary          = Honey500,
    onPrimary        = Espresso,
    primaryContainer = Honey300,
    onPrimaryContainer = Espresso,
    secondary        = Espresso,
    onSecondary      = SurfaceLight,
    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = TextPrimaryLight,
    background       = BackgroundLight,
    onBackground     = TextPrimaryLight,
    surface          = SurfaceLight,
    onSurface        = TextPrimaryLight,
    surfaceVariant   = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline          = DividerLight,
    error            = SemanticError,
    onError          = SurfaceLight,
)

private val LightChurrosColors = ChurrosColors(
    textSecondary     = TextSecondaryLight,
    textTertiary      = TextTertiaryLight,
    textDisabled      = TextDisabledLight,
    divider           = DividerLight,
    surfaceElevated   = SurfaceVariantLight,
    success           = SemanticSuccess,
    info              = SemanticInfo,
    event             = SemanticEvent,
    warning           = SemanticWarning,
    error             = SemanticError,
    stickyShoppingBackground  = StickyShoppingLight,
    stickyChoresBackground    = StickyChoresLight,
    stickyReminderBackground  = StickyReminderLight,
    stickyEventBackground     = StickyEventLight,
    stickyImportantBackground = StickyImportantLight,
)

// ── Dark color scheme ─────────────────────────────────────────────────────────

private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary          = Honey500,
    onPrimary        = Espresso,
    primaryContainer = Honey600,
    onPrimaryContainer = TextPrimaryDark,
    secondary        = EspressoAccentDark,
    onSecondary      = BackgroundDark,
    secondaryContainer = SurfaceElevatedDark,
    onSecondaryContainer = TextPrimaryDark,
    background       = BackgroundDark,
    onBackground     = TextPrimaryDark,
    surface          = SurfaceDark,
    onSurface        = TextPrimaryDark,
    surfaceVariant   = SurfaceElevatedDark,
    onSurfaceVariant = TextSecondaryDark,
    outline          = DividerDark,
    error            = SemanticError,
    onError          = SurfaceDark,
)

private val DarkChurrosColors = ChurrosColors(
    textSecondary     = TextSecondaryDark,
    textTertiary      = EspressoAccentDark,
    textDisabled      = EspressoAccentDark,
    divider           = DividerDark,
    surfaceElevated   = SurfaceElevatedDark,
    success           = SemanticSuccess,
    info              = SemanticInfo,
    event             = SemanticEvent,
    warning           = SemanticWarning,
    error             = SemanticError,
    stickyShoppingBackground  = StickyShoppingDark,
    stickyChoresBackground    = StickyChoresDark,
    stickyReminderBackground  = StickyReminderDark,
    stickyEventBackground     = StickyEventDark,
    stickyImportantBackground = StickyImportantDark,
)

// ── ChurrosTheme ──────────────────────────────────────────────────────────────

/**
 * The root theme composable for Churros.
 *
 * Wraps [MaterialTheme] with:
 * - Warm Honey/Espresso color scheme (light + dark)
 * - Manrope typography (loaded from bundled TTF resources)
 * - Extended [ChurrosColors] accessible via [MaterialTheme.churrosColors]
 *
 * Usage:
 * ```kotlin
 * ChurrosTheme {
 *     // your UI
 * }
 * ```
 */
@Composable
fun ChurrosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val churrosColors = if (darkTheme) DarkChurrosColors else LightChurrosColors
    val typography = churrosTypography()

    CompositionLocalProvider(LocalChurrosColors provides churrosColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = churrosShapes,
            content = content,
        )
    }
}
