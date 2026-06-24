package com.example.mindflow.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MindFlowColors(
    val authCard: Color,
    val authCardBorder: Color,
    val input: Color,
    val inputFocusedBorder: Color,
    val inputUnfocusedBorder: Color,
    val inputPlaceholder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textLabel: Color,
    val ctaText: Color,
    val backgroundGlow: Color,
    val backgroundGlowAlpha: Float
)

internal val LocalMindFlowColors = staticCompositionLocalOf {
    MindFlowColors(
        authCard = SurfaceLight,
        authCardBorder = BorderLight,
        input = InputLight,
        inputFocusedBorder = PrimaryLight,
        inputUnfocusedBorder = BorderLight,
        inputPlaceholder = PlaceholderLight,
        textPrimary = PrimaryLight,
        textSecondary = SubtitleLight,
        textLabel = LabelLight,
        ctaText = LabelLight,
        backgroundGlow = GradientCenter,
        backgroundGlowAlpha = 0.10f
    )
}

internal val LightMindFlowColors = MindFlowColors(
    authCard = SurfaceLight,
    authCardBorder = BorderLight,
    input = InputLight,
    inputFocusedBorder = PrimaryLight,
    inputUnfocusedBorder = BorderLight,
    inputPlaceholder = PlaceholderLight,
    textPrimary = PrimaryLight,
    textSecondary = SubtitleLight,
    textLabel = LabelLight,
    ctaText = LabelLight,
    backgroundGlow = GradientCenter,
    backgroundGlowAlpha = 0.10f
)

internal val DarkMindFlowColors = MindFlowColors(
    authCard = SurfaceDark,
    authCardBorder = BorderDark,
    input = InputDark,
    inputFocusedBorder = PrimaryDark,
    inputUnfocusedBorder = BorderDark,
    inputPlaceholder = PlaceholderDark,
    textPrimary = PrimaryDark,
    textSecondary = SubtitleDark,
    textLabel = LabelDark,
    ctaText = LabelDark,
    backgroundGlow = MindFlowPurple,
    backgroundGlowAlpha = 0.055f
)

val MaterialTheme.mindFlowColors: MindFlowColors
    @Composable get() = LocalMindFlowColors.current
