package com.example.mindflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mindflow.ui.theme.mindFlowColors

@Composable
fun MindFlowAuthBrandHeader(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.mindFlowColors

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MindFlowLogoMark()

            Column {
                Text(
                    text = "MindFlow",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Voice notes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(999.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = colors.textLabel
        ) {
            Text(
                text = "Beta",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MindFlowLogoMark(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .size(48.dp)
                .shadow(10.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(13.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {}
    }
}
