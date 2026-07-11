package com.therxmv.churros.core.design

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.Manrope_Bold
import churros.shared.generated.resources.Manrope_Medium
import churros.shared.generated.resources.Manrope_Regular
import churros.shared.generated.resources.Manrope_SemiBold
import org.jetbrains.compose.resources.Font

@Composable
fun manropeFontFamily(): FontFamily = FontFamily(
    Font(Res.font.Manrope_Regular, weight = FontWeight.Normal),
    Font(Res.font.Manrope_Medium, weight = FontWeight.Medium),
    Font(Res.font.Manrope_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.Manrope_Bold, weight = FontWeight.Bold),
)

/**
 * Churros typography scale.
 *
 * | Style        | Weight | Size |
 * |--------------|--------|------|
 * | displayLarge | 700    | 40sp |
 * | displayMedium| 700    | 34sp |
 * | headlineLarge| 700    | 28sp |
 * | titleLarge   | 600    | 22sp |
 * | titleMedium  | 600    | 18sp |
 * | bodyLarge    | 500    | 16sp |
 * | bodyMedium   | 400    | 14sp |
 * | labelLarge   | 600    | 13sp |
 * | labelSmall   | 500    | 12sp |
 */
@Composable
fun churrosTypography(): Typography {
    val manrope = manropeFontFamily()
    return Typography(
        displayLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            lineHeight = (40 * 1.2).sp,
            letterSpacing = 0.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            lineHeight = (34 * 1.2).sp,
            letterSpacing = 0.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = (28 * 1.2).sp,
            letterSpacing = 0.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = (28 * 1.2).sp,
            letterSpacing = 0.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = (24 * 1.2).sp,
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = (22 * 1.2).sp,
            letterSpacing = 0.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = (22 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = (18 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = (16 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = (16 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = (14 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = (12 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = (13 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = (12 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = (12 * 1.4).sp,
            letterSpacing = 0.sp,
        ),
    )
}
