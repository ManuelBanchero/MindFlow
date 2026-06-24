package com.example.mindflow.ui.presentation.ideadetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.StructuredSection
import com.example.mindflow.ui.components.MindFlowBackground
import com.example.mindflow.ui.components.MindFlowButton
import com.example.mindflow.ui.theme.mindFlowColors
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun IdeaDetailContent(
    uiState: IdeaDetailUiState,
    onEvent: (IdeaDetailEvent) -> Unit,
    onBack: () -> Unit,
    onNavigateToQuestions: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val idea = uiState.idea
    val isDark = isSystemInDarkTheme()

    DetailBackground(
        modifier = modifier,
        usePlainBackground = isDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 430.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                IdeaDetailTopBar(
                    onBack = onBack,
                    onEdit = { onEvent(IdeaDetailEvent.OnEditClick) },
                    onDelete = { onEvent(IdeaDetailEvent.OnDeleteClick) }
                )

                Spacer(modifier = Modifier.height(26.dp))

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.error != null -> {
                        DetailMessageState(
                            title = "No pudimos abrir esta idea",
                            message = uiState.error,
                            actionText = "Volver al historial",
                            onAction = onBack
                        )
                    }

                    idea == null -> {
                        DetailMessageState(
                            title = "Idea no encontrada",
                            message = "No hay una idea disponible para mostrar en este momento.",
                            actionText = "Volver al historial",
                            onAction = onBack
                        )
                    }

                    else -> {
                        DetailScrollableContent(
                            idea = idea,
                            onEvent = onEvent,
                            onNavigateToQuestions = onNavigateToQuestions
                        )
                    }
                }
            }
        }
    }

    if (uiState.showDeleteDialog && idea != null) {
        AlertDialog(
            onDismissRequest = { onEvent(IdeaDetailEvent.OnDismissDelete) },
            title = {
                Text(
                    text = "Eliminar idea",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Esta acción elimina la idea y sus preguntas guardadas.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onEvent(IdeaDetailEvent.OnConfirmDelete) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(IdeaDetailEvent.OnDismissDelete) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun DetailBackground(
    modifier: Modifier = Modifier,
    usePlainBackground: Boolean,
    content: @Composable () -> Unit
) {
    if (usePlainBackground) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopStart
        ) {
            content()
        }
    } else {
        MindFlowBackground(modifier = modifier) {
            content()
        }
    }
}

@Composable
private fun DetailScrollableContent(
    idea: Idea,
    onEvent: (IdeaDetailEvent) -> Unit,
    onNavigateToQuestions: (Int) -> Unit
) {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    val primaryTitleColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
    val subtitleColor = if (isDark) Color(0xFFCAC4D0) else MaterialTheme.colorScheme.onSurfaceVariant
    val bodyColor = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary
    val labelColor = if (isDark) Color(0xFFB7ABC8) else colors.textLabel

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Text(
            text = idea.categories.takeIf { it.isNotEmpty() }
                ?.joinToString(" · ")
                ?.uppercase()
                ?: "IDEA",
            style = MaterialTheme.typography.labelLarge,
            color = labelColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = idea.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                lineHeight = MaterialTheme.typography.headlineMedium.lineHeight
            ),
            color = primaryTitleColor,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = formatIdeaDate(idea.createdAt),
            style = MaterialTheme.typography.bodyMedium,
            color = subtitleColor,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(18.dp))

        MindFlowButton(
            text = "Preguntas para profundizar tu idea",
            onClick = { onNavigateToQuestions(idea.id) }
        )

        Spacer(modifier = Modifier.height(18.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = if (isDark) Color(0xFF1C1B1B) else MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                1.dp,
                if (isDark) Color(0xFF2E2938) else MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDark) Color(0xFF1C1B1B) else MaterialTheme.colorScheme.surface)
                    .padding(18.dp)
            ) {
                Column {
                    SectionHeader(
                        title = "Resumen",
                        action = null
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailQuoteBlock(
                        text = idea.summarizeContent.ifBlank {
                            idea.structuredIdea.firstOrNull()?.content ?: "Sin resumen disponible."
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        SectionHeader(
            title = "Estructura detallada",
            action = null
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (idea.structuredIdea.isEmpty()) {
            DetailEmptyInline(
                text = "Aún no hay una estructura detallada para esta idea."
            )
        } else {
            idea.structuredIdea.forEach { section ->
                StructuredSectionCard(section = section)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        MindFlowButton(
            text = "Seguí profundizando tu idea",
            onClick = { },
            enabled = false
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun IdeaDetailTopBar(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DetailIconButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            onClick = onBack
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailIconButton(
                icon = Icons.Filled.Edit,
                onClick = onEdit
            )
            DetailIconButton(
                icon = Icons.Filled.Delete,
                onClick = onDelete
            )
        }
    }
}

@Composable
private fun DetailIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val containerColor = if (isDark) Color(0xFF1B1527) else MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
    val contentColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary

    Surface(
        modifier = Modifier.size(42.dp),
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.12f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String?
) {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()
    val titleColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
    val secondaryColor = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = titleColor,
            fontWeight = FontWeight.ExtraBold
        )

        if (action != null) {
            Text(
                text = action,
                style = MaterialTheme.typography.labelLarge,
                color = secondaryColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DetailQuoteBlock(text: String) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isDark) Color(0xFF1C1B1B) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF3B3347) else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        ),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(96.dp)
                    .background(
                        if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(999.dp)
                    )
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun StructuredSectionCard(section: StructuredSection) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors
    val titleColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
    val bodyColor = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = if (isDark) Color(0xFF1C1B1B) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF2E2938) else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        ),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = prettySectionTitle(section.title, section.type.name),
                style = MaterialTheme.typography.titleMedium,
                color = titleColor,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = section.content,
                style = MaterialTheme.typography.bodyMedium,
                color = bodyColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DetailEmptyInline(text: String) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isDark) Color(0xFF1C1B1B).copy(alpha = 0.88f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF2E2938) else MaterialTheme.colorScheme.outline.copy(alpha = 0.16f)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DetailMessageState(
    title: String,
    message: String,
    actionText: String,
    onAction: () -> Unit
) {
    val colors = MaterialTheme.mindFlowColors

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = colors.textPrimary,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.textSecondary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        MindFlowButton(
            text = actionText,
            onClick = onAction,
            icon = Icons.AutoMirrored.Filled.ArrowBack
        )
    }
}

@Composable
private fun prettySectionTitle(rawTitle: String, fallback: String): String {
    val candidate = rawTitle.takeIf { it.isNotBlank() } ?: fallback.replace('_', ' ')
    return candidate.lowercase()
        .split(" ")
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}

private fun formatIdeaDate(instant: java.time.Instant): String {
    val formatter = DateTimeFormatter
        .ofPattern("dd MMM, HH:mm")
        .withZone(ZoneId.systemDefault())

    return formatter.format(instant)
}
