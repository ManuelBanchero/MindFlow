package com.example.mindflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.mindflow.ui.theme.mindFlowColors

@Composable
fun MindFlowBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = MaterialTheme.mindFlowColors
    val backgroundBrush = Brush.radialGradient(
        colors = listOf(
            colors.backgroundGlow.copy(alpha = colors.backgroundGlowAlpha),
            MaterialTheme.colorScheme.background
        ),
        radius = 1100f
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush),
        content = content
    )
}
