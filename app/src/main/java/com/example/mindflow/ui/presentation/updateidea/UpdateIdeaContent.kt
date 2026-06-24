package com.example.mindflow.ui.presentation.updateidea

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mindflow.domain.model.StructuredSectionType
import com.example.mindflow.ui.components.MindFlowBackground
import com.example.mindflow.ui.components.MindFlowButton
import com.example.mindflow.ui.theme.mindFlowColors

@Composable
fun UpdateIdeaContent(
    uiState: UpdateIdeaUiState,
    onEvent: (UpdateIdeaEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    UpdateIdeaBackground(
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                UpdateIdeaTopBar(onBack = onBack)

                Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Edita tu Idea",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )

                Spacer(modifier = Modifier.height(24.dp))

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Cargando idea...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    uiState.error != null && uiState.idea == null -> {
                        UpdateIdeaMessage(
                            title = "No pudimos abrir la idea",
                            message = uiState.error,
                            actionText = "Volver",
                            onAction = onBack
                        )
                    }

                    uiState.idea == null -> {
                        UpdateIdeaMessage(
                            title = "Idea no encontrada",
                            message = "No hay una idea disponible para editar en este momento.",
                            actionText = "Volver",
                            onAction = onBack
                        )
                    }

                    else -> {
                        UpdateIdeaForm(
                            uiState = uiState,
                            onEvent = onEvent,
                            onBack = onBack
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpdateIdeaBackground(
    modifier: Modifier = Modifier,
    usePlainBackground: Boolean,
    content: @Composable () -> Unit
) {
    if (usePlainBackground) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
private fun UpdateIdeaTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            onClick = onBack,
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.8f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.82f)
            },
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UpdateIdeaForm(
    uiState: UpdateIdeaUiState,
    onEvent: (UpdateIdeaEvent) -> Unit,
    onBack: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors
    val scrollState = rememberScrollState()
    val fieldContainer = if (isDark) Color(0xFF201A29) else MaterialTheme.colorScheme.surface
    val sectionContainer = if (isDark) Color(0xFF1C1B1B) else MaterialTheme.colorScheme.surface
    val primaryTextColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
    val secondaryTextColor = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary
    val labelColor = if (isDark) Color(0xFFE0D6F2) else colors.textLabel

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        FormFieldLabel(text = "Título", color = labelColor)
        Spacer(modifier = Modifier.height(8.dp))
        UpdateIdeaTextField(
            value = uiState.title,
            onValueChange = { onEvent(UpdateIdeaEvent.OnTitleChange(it)) },
            placeholder = "Escribí el título de tu idea",
            singleLine = true,
            containerColor = fieldContainer,
            textColor = secondaryTextColor,
            accentColor = primaryTextColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormFieldLabel(text = "Categorías", color = labelColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Mínimo 1 y máximo 3 categorías",
            style = MaterialTheme.typography.bodySmall,
            color = secondaryTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.categories.forEachIndexed { index, category ->
                CategoryChip(
                    text = category,
                    onRemove = { onEvent(UpdateIdeaEvent.OnRemoveCategoryClick(index)) },
                    selected = true
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(modifier = Modifier.weight(1f)) {
                UpdateIdeaTextField(
                    value = uiState.categoryInput,
                    onValueChange = {
                        onEvent(UpdateIdeaEvent.OnCategoryInputChange(it))
                    },
                    placeholder = "Nueva categoría",
                    singleLine = true,
                    containerColor = fieldContainer,
                    textColor = secondaryTextColor,
                    accentColor = primaryTextColor
                )
            }

            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = if (uiState.categories.size < 3) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                },
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Box(
                    modifier = Modifier.clickable(enabled = uiState.categories.size < 3) {
                        onEvent(UpdateIdeaEvent.OnAddCategoryClick)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Agregar categoría",
                        tint = if (uiState.categories.size < 3) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.55f)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        FormFieldLabel(text = "Resumen", color = labelColor)
        Spacer(modifier = Modifier.height(8.dp))
        UpdateIdeaTextField(
            value = uiState.summary,
            onValueChange = { onEvent(UpdateIdeaEvent.OnSummaryChange(it.take(180))) },
            placeholder = "Escribí un resumen corto",
            singleLine = false,
            minLines = 5,
            maxLines = 7,
            containerColor = fieldContainer,
            textColor = secondaryTextColor,
            accentColor = primaryTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${uiState.summary.length.coerceAtMost(180)}/180",
            style = MaterialTheme.typography.labelMedium,
            color = secondaryTextColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        FormFieldLabel(text = "Estructura detallada", color = labelColor)
        Spacer(modifier = Modifier.height(8.dp))

        uiState.sections.forEachIndexed { index, section ->
            EditableSectionCard(
                index = index,
                section = section,
                onTypeChange = { onEvent(UpdateIdeaEvent.OnSectionTypeChange(index, it)) },
                onTitleChange = { onEvent(UpdateIdeaEvent.OnSectionTitleChange(index, it)) },
                onContentChange = { onEvent(UpdateIdeaEvent.OnSectionContentChange(index, it)) },
                onDelete = { onEvent(UpdateIdeaEvent.OnRemoveSectionClick(index)) },
                labelColor = labelColor,
                bodyColor = secondaryTextColor,
                accentColor = primaryTextColor,
                containerColor = sectionContainer
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        TextButton(
            onClick = { onEvent(UpdateIdeaEvent.OnAddSectionClick) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "Agregar otra sección",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (uiState.error != null) {
            UpdateIdeaInlineError(message = uiState.error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedActionButton(
                text = "Descartar",
                onClick = onBack,
                modifier = Modifier.weight(1f),
                isDark = isDark
            )
            MindFlowButton(
                text = "Guardar",
                onClick = { onEvent(UpdateIdeaEvent.OnSaveClick) },
                modifier = Modifier.weight(1f),
                isLoading = uiState.isSaving,
                enabled = !uiState.isLoading && !uiState.isSaving
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun FormFieldLabel(text: String, color: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = color,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun UpdateIdeaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    containerColor: Color,
    textColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else 4
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor.copy(alpha = 0.7f)
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = textColor,
            fontWeight = FontWeight.Medium
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor.copy(alpha = 0.8f),
            focusedBorderColor = accentColor.copy(alpha = 0.75f),
            unfocusedBorderColor = if (isSystemInDarkTheme()) Color(0xFF3B3347) else MaterialTheme.colorScheme.outline.copy(alpha = 0.65f),
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = accentColor,
            focusedPlaceholderColor = textColor.copy(alpha = 0.6f),
            unfocusedPlaceholderColor = textColor.copy(alpha = 0.6f)
        )
    )
}

@Composable
private fun EditableSectionCard(
    index: Int,
    section: UpdateIdeaSectionUiState,
    onTypeChange: (StructuredSectionType) -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onDelete: () -> Unit,
    labelColor: Color,
    bodyColor: Color,
    accentColor: Color,
    containerColor: Color
) {
    val isDark = isSystemInDarkTheme()
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF2E2938) else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        ),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sección ${index + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    color = labelColor,
                    fontWeight = FontWeight.ExtraBold
                )

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar sección",
                        tint = labelColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = containerColor,
                    contentColor = bodyColor,
                    border = BorderStroke(
                        1.dp,
                        if (isDark) Color(0xFF3B3347) else MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Tipo de sección",
                                style = MaterialTheme.typography.labelLarge,
                                color = labelColor,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = section.type.displayName(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = bodyColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = bodyColor
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    StructuredSectionType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = type.displayName(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            onClick = {
                                onTypeChange(type)
                                expanded = false
                            },
                            trailingIcon = if (type == section.type) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null
                                    )
                                }
                            } else null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            UpdateIdeaTextField(
                value = section.title,
                onValueChange = onTitleChange,
                placeholder = "Título de la sección",
                singleLine = true,
                containerColor = if (isDark) Color(0xFF201A29) else MaterialTheme.colorScheme.surface,
                textColor = bodyColor,
                accentColor = accentColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            UpdateIdeaTextField(
                value = section.content,
                onValueChange = onContentChange,
                placeholder = "Escribí el contenido de esta sección",
                singleLine = false,
                minLines = 4,
                maxLines = 7,
                containerColor = if (isDark) Color(0xFF201A29) else MaterialTheme.colorScheme.surface,
                textColor = bodyColor,
                accentColor = accentColor
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Eliminar sección",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            },
            text = {
                Text(
                    text = "¿Seguro que querés eliminar esta sección?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun CategoryChip(
    text: String,
    onRemove: () -> Unit,
    selected: Boolean
) {
    val isDark = isSystemInDarkTheme()
    val container = if (selected) {
        if (isDark) Color(0xFF4A4359) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    val labelColor = if (isDark) Color(0xFFBAB1CA) else MaterialTheme.colorScheme.primary

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = container,
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF5D5670) else MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = labelColor,
                fontWeight = FontWeight.SemiBold
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Eliminar categoría",
                    modifier = Modifier.size(16.dp),
                    tint = labelColor
                )
            }
        }
    }
}

@Composable
private fun OutlinedActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean
) {
    val container = if (isDark) Color(0xFF23202A) else MaterialTheme.colorScheme.surface
    val content = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .shadow(2.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        color = container,
        contentColor = content,
        border = BorderStroke(1.dp, content.copy(alpha = 0.10f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun UpdateIdeaMessage(
    title: String,
    message: String,
    actionText: String,
    onAction: () -> Unit
) {
    val colors = MaterialTheme.mindFlowColors

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))
        MindFlowButton(
            text = actionText,
            onClick = onAction
        )
    }
}

@Composable
private fun UpdateIdeaInlineError(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun StructuredSectionType.displayName(): String {
    return name.lowercase()
        .split('_')
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}
