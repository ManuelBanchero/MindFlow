package com.example.mindflow.ui.presentation.idealist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.ui.components.MindFlowBackground
import com.example.mindflow.ui.theme.mindFlowColors
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun IdeaListContent(
    uiState: IdeaListUiState,
    onNavigateToCreateIdea: () -> Unit,
    onNavigateToLogout: () -> Unit,
    onIdeaClick: (Int) -> Unit
) {
    IdeaListBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 430.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                IdeaListTopBar(
                    onNavigateToLogout = onNavigateToLogout
                )

                Spacer(modifier = Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when {
                        uiState.isLoading -> IdeaListLoadingState()
                        uiState.error != null -> IdeaListErrorState(uiState.error)
                        uiState.ideas.isEmpty() -> EmptyIdeaState()
                        else -> IdeasState(
                            ideas = uiState.ideas,
                            onIdeaClick = onIdeaClick
                        )
                    }
                }

                IdeaListBottomNavigation(
                    onNavigateToCreateIdea = onNavigateToCreateIdea
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun IdeaListBackground(
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    if (!isDark) {
        MindFlowBackground {
            content()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B090F))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF3D3548).copy(alpha = 0.26f),
                        Color.Transparent
                    ),
                    radius = 680f
                )
            )
    ) {
        content()
    }
}

@Composable
private fun IdeaListTopBar(
    onNavigateToLogout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopIconButton(
                icon = Icons.Default.Menu,
                contentDescription = "Abrir menú",
                onClick = { }
            )

            Text(
                text = "Mis Ideas",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        TopIconButton(
            icon = Icons.Default.Person,
            contentDescription = "Perfil",
            onClick = onNavigateToLogout
        )
    }
}

@Composable
private fun TopIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Surface(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        color = if (isDark) Color(0xFF17131F) else MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun IdeaListLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun IdeaListErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(18.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyIdeaState() {
    val colors = MaterialTheme.mindFlowColors

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(176.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(24.dp, CircleShape),
                shape = CircleShape,
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.34f)
                                )
                            )
                        )
                )
            }

            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                modifier = Modifier.size(38.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Tu espacio de ideas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Aún no tenés reflexiones. ¡Empezá ahora!",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(34.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp)
                .shadow(18.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Crea tu primera reflexión",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun IdeasState(
    ideas: List<Idea>,
    onIdeaClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        IdeaListHeader(count = ideas.size)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = ideas,
                key = { idea -> idea.id }
            ) { idea ->
                IdeaCard(
                    idea = idea,
                    onClick = { onIdeaClick(idea.id) }
                )
            }

            item {
                IdeaListFooter()
            }
        }
    }
}

@Composable
private fun IdeaListHeader(count: Int) {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "RECIENTES",
                style = MaterialTheme.typography.labelLarge,
                color = if (isDark) Color(0xFFE9DDFF) else colors.textLabel,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (count == 1) {
                    "Tenés 1 reflexión capturada"
                } else {
                    "Tenés $count reflexiones capturadas"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary,
                fontWeight = FontWeight.Medium
            )
        }

        Surface(
            onClick = { },
            shape = RoundedCornerShape(12.dp),
            color = if (isDark) Color(0xFF111015) else MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            contentColor = if (isDark) Color(0xFFE9DDFF) else colors.textLabel,
            border = BorderStroke(
                1.dp,
                if (isDark) Color(0xFF4A4359) else MaterialTheme.colorScheme.outline.copy(alpha = 0.62f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Filtrar",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IdeaCard(
    idea: Idea,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()
    val preview = idea.summarizeContent.ifBlank {
        idea.structuredIdea.firstOrNull()?.content ?: "Sin resumen disponible."
    }
    val tags = buildList {
        idea.categories.take(3).forEach { category ->
            if (category.isNotBlank()) add(category)
        }
    }.take(3)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isDark) Color(0xFF1C1B1B) else MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF2E2938) else MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isDark) {
                        Brush.linearGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF000000),
                                0.72f to Color(0xFF111015),
                                0.9f to Color(0xFF1C1B1B),
                                1f to Color(0xFF666666).copy(alpha = 0.32f)
                            ),
                            start = Offset(0f, 520f),
                            end = Offset(980f, -120f)
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
                            )
                        )
                    }
                )
                .padding(18.dp)
        ) {
            Column {
                Text(
                    text = idea.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 22.sp,
                        lineHeight = 26.sp
                    ),
                    color = if (isDark) Color(0xFFF4EBFF) else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatIdeaDate(idea.createdAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color(0xFFCAC4D0) else colors.textLabel,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = preview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            IdeaTag(text = tag.trim())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IdeaTag(text: String) {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = if (isDark) Color(0xFF26212D) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
        contentColor = if (isDark) Color(0xFF9F94AE) else colors.textLabel
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun IdeaListFooter() {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(42.dp),
            tint = if (isDark) Color(0xFF4A4359) else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Fin de tus pensamientos guardados",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun IdeaListBottomNavigation(
    onNavigateToCreateIdea: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = if (isDark) Color(0xFF1C1B1B).copy(alpha = 0.94f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.74f),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                text = "Capturar",
                icon = Icons.Default.Mic,
                selected = false,
                onClick = onNavigateToCreateIdea
            )
            BottomNavItem(
                text = "Historial",
                icon = Icons.Default.History,
                selected = true,
                onClick = { }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()
    val containerColor = if (selected) {
        if (isDark) Color(0xFF4A4359) else MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    val contentColor = if (selected) {
        if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        if (isDark) Color(0xFFCAC4D0) else colors.textSecondary
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun formatIdeaDate(instant: java.time.Instant): String {
    val formatter = DateTimeFormatter
        .ofPattern("dd MMM, HH:mm")
        .withZone(ZoneId.systemDefault())

    return formatter.format(instant)
}
