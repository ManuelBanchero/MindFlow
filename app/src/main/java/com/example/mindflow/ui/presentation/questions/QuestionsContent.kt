package com.example.mindflow.ui.presentation.questions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.Question
import com.example.mindflow.domain.service.RecordingState
import com.example.mindflow.ui.components.MindFlowBackground
import com.example.mindflow.ui.components.MindFlowButton
import com.example.mindflow.ui.components.MindFlowCircularActionButton
import com.example.mindflow.ui.theme.mindFlowColors
import kotlin.math.roundToInt

@Composable
fun QuestionsContent(
    uiState: QuestionsUiState,
    onEvent: (QuestionsEvent) -> Unit,
    onBack: () -> Unit,
    onNavigateToCreateIdea: () -> Unit,
    onNavigateToIdeaList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val idea = uiState.idea
    val questions = idea?.questions.orEmpty()
    val answeredCount = questions.count { it.wasAnswered }
    val progress = if (questions.isEmpty()) 1f else answeredCount.toFloat() / questions.size.toFloat()
    val completionText = "${(progress * 100).roundToInt()}% Completado"
    val allAnswered = questions.isNotEmpty() && answeredCount == questions.size
    val initialPage = remember(questions) {
        questions.indexOfFirst { !it.wasAnswered }.takeIf { it >= 0 } ?: 0
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { questions.size.coerceAtLeast(1) }
    )
    val currentPage = if (questions.isEmpty()) 0 else pagerState.currentPage.coerceIn(0, questions.lastIndex)
    val currentQuestion = questions.getOrNull(currentPage)
    val context = LocalContext.current
    var pendingQuestionId by rememberSaveable { mutableIntStateOf(-1) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted && pendingQuestionId != -1) {
                onEvent(QuestionsEvent.OnToggleRecord(pendingQuestionId))
            }
            pendingQuestionId = -1
        }
    )

    val onRecordClick: (Int) -> Unit = { questionId ->
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            onEvent(QuestionsEvent.OnToggleRecord(questionId))
        } else {
            pendingQuestionId = questionId
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    QuestionsBackground(modifier = modifier) {
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
                Spacer(modifier = Modifier.height(20.dp))

                QuestionsTopBar(onBack = onBack)

                Spacer(modifier = Modifier.height(18.dp))

                QuestionsProgressHeader(
                    questionLabel = if (questions.isEmpty() || allAnswered) {
                        "NO HAY PREGUNTAS"
                    } else {
                        "PREGUNTA ${currentPage + 1} DE ${questions.size}"
                    },
                    progress = progress,
                    completionText = completionText,
                    ideaTitle = idea?.title.orEmpty()
                )

                Spacer(modifier = Modifier.height(18.dp))

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    idea == null -> {
                        QuestionsMessageState(
                            title = "No encontramos esta idea",
                            message = uiState.error ?: "No hay una idea disponible para trabajar sus preguntas."
                        )
                    }

                    questions.isEmpty() || allAnswered -> {
                        QuestionsEmptyState()
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    else -> {
                        BoxWithConstraints(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            val pageWidth = if (maxWidth > 380.dp) {
                                maxWidth - 56.dp
                            } else {
                                maxWidth - 44.dp
                            }
                            val sidePeekPadding = (maxWidth - pageWidth) / 2

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                pageSize = PageSize.Fixed(pageWidth),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = sidePeekPadding),
                                pageSpacing = 18.dp,
                                userScrollEnabled = uiState.activeQuestionId == null && !uiState.isSubmitting
                            ) { page ->
                                QuestionCard(
                                    question = questions[page],
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(26.dp))

                        if (currentQuestion != null) {
                            QuestionAnswerControls(
                                question = currentQuestion,
                                recordingState = uiState.recordingState,
                                isSubmitting = uiState.isSubmitting && uiState.activeQuestionId == currentQuestion.id,
                                isActiveQuestion = uiState.activeQuestionId == currentQuestion.id,
                                onRecordClick = { onRecordClick(currentQuestion.id) },
                                onPauseClick = { onEvent(QuestionsEvent.OnTogglePause(currentQuestion.id)) },
                                onCancelClick = { onEvent(QuestionsEvent.OnCancelRecord(currentQuestion.id)) }
                            )
                        }
                    }
                }

                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(18.dp))
                    QuestionsErrorMessage(
                        message = uiState.error,
                        onDismiss = { onEvent(QuestionsEvent.OnDismissError) }
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                QuestionsBottomNavigation(
                    onNavigateToCreateIdea = onNavigateToCreateIdea,
                    onNavigateToIdeaList = onNavigateToIdeaList
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun QuestionsBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    if (!isDark) {
        MindFlowBackground(modifier = modifier) {
            content()
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B090F))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF3D3548).copy(alpha = 0.24f),
                        Color.Transparent
                    ),
                    radius = 720f
                )
            )
    ) {
        content()
    }
}

@Composable
private fun QuestionsTopBar(
    onBack: () -> Unit
) {
    Surface(
        onClick = onBack,
        modifier = Modifier.size(42.dp),
        shape = CircleShape,
        color = if (isSystemInDarkTheme()) Color(0xFF17131F) else MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
        contentColor = if (isSystemInDarkTheme()) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun QuestionsProgressHeader(
    questionLabel: String,
    progress: Float,
    completionText: String,
    ideaTitle: String
) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = questionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = if (isDark) Color(0xFFCAC4D0) else colors.textLabel,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = completionText,
                style = MaterialTheme.typography.labelLarge,
                color = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = if (isDark) Color(0xFF343139) else MaterialTheme.colorScheme.outline.copy(alpha = 0.24f)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
            )
            Text(
                text = ideaTitle,
                style = MaterialTheme.typography.titleMedium,
                color = if (isDark) Color(0xFFE9DDFF) else colors.textLabel,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuestionCard(
    question: Question,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors

    Surface(
        modifier = modifier
            .padding(top = 8.dp)
            .shadow(8.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        color = if (isDark) Color(0xFF1C1B1B) else MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0xFF2E2938) else MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isDark) {
                        Brush.linearGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF111015),
                                0.72f to Color(0xFF1C1B1B),
                                1f to Color(0xFF666666).copy(alpha = 0.24f)
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
                .padding(horizontal = 28.dp, vertical = 30.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = if (isDark) Color(0xFFCCB7FF) else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isDark) Color(0xFF5A3BA5) else colors.textLabel
                ) {
                    Text(
                        text = question.category.uppercase(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = question.questionText,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isDark) Color(0xFFF4EBFF) else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )

                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(0.16f)
                        .background(
                            if (isDark) Color(0xFF766F83) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                            RoundedCornerShape(999.dp)
                        )
                )

                Text(
                    text = question.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun QuestionAnswerControls(
    question: Question,
    recordingState: RecordingState,
    isSubmitting: Boolean,
    isActiveQuestion: Boolean,
    onRecordClick: () -> Unit,
    onPauseClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors
    val isRecording = isActiveQuestion && recordingState is RecordingState.Recording
    val isPaused = isActiveQuestion && recordingState is RecordingState.Paused
    val showSecondaryActions = isActiveQuestion && (isRecording || isPaused)

    if (question.wasAnswered) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MindFlowCircularActionButton(
                onClick = {},
                enabled = false,
                icon = Icons.Default.Check,
                contentDescription = "Pregunta respondida",
                size = 110.dp,
                iconSize = 42.dp,
                shadowElevation = 24.dp,
                containerColor = Color(0xFFC8FFD4),
                contentColor = Color(0xFF164E2C)
            )
        }
        return
    }

    if (showSecondaryActions) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp),
            contentAlignment = Alignment.Center
        ) {
            MindFlowCircularActionButton(
                onClick = onRecordClick,
                isLoading = isSubmitting,
                icon = Icons.Default.Stop,
                contentDescription = "Finalizar respuesta",
                size = 112.dp,
                iconSize = 40.dp,
                shadowElevation = 24.dp,
                containerColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary,
                contentColor = if (isDark) Color(0xFF2F195E) else MaterialTheme.colorScheme.onPrimary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmallControlButton(
                    icon = Icons.Default.Close,
                    onClick = onCancelClick,
                    containerColor = if (isDark) Color(0xFF2B272F) else MaterialTheme.colorScheme.surface,
                    contentColor = if (isDark) Color(0xFFFFB2B2) else MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.width(112.dp))

                SmallControlButton(
                    icon = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    onClick = onPauseClick,
                    containerColor = if (isDark) Color(0xFF2B272F) else MaterialTheme.colorScheme.surface,
                    contentColor = if (isDark) Color(0xFFE9DDFF) else colors.textLabel
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MindFlowCircularActionButton(
                onClick = onRecordClick,
                isLoading = isSubmitting,
                icon = Icons.Default.Mic,
                contentDescription = "Responder por audio",
                size = 112.dp,
                iconSize = 40.dp,
                shadowElevation = 24.dp,
                containerColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary,
                contentColor = if (isDark) Color(0xFF2F195E) else MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun SmallControlButton(
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun QuestionsEmptyState() {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(176.dp)
                .shadow(18.dp, CircleShape),
            shape = CircleShape,
            color = if (isDark) Color(0xFF17131F) else MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
            border = BorderStroke(
                1.dp,
                if (isDark) Color(0xFF2A2631) else MaterialTheme.colorScheme.outline.copy(alpha = 0.16f)
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Espacio de preguntas",
            style = MaterialTheme.typography.headlineSmall,
            color = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Ya respondiste todas tus preguntas. ¿Querés crear nuevas?",
            style = MaterialTheme.typography.bodyLarge,
            color = if (isDark) Color(0xFFCAC4D0) else colors.textSecondary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        MindFlowButton(
            text = "Crea más preguntas",
            onClick = {}
        )
    }
}

@Composable
private fun QuestionsMessageState(
    title: String,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.mindFlowColors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun QuestionsErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Surface(
                onClick = onDismiss,
                shape = CircleShape,
                color = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    modifier = Modifier
                        .padding(2.dp)
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun QuestionsBottomNavigation(
    onNavigateToCreateIdea: () -> Unit,
    onNavigateToIdeaList: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.mindFlowColors

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = if (isDark) {
            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.78f)
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.74f)
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuestionBottomNavItem(
                text = "Capturar",
                icon = Icons.Default.Mic,
                selected = false,
                contentColor = colors.textSecondary,
                onClick = onNavigateToCreateIdea
            )

            QuestionBottomNavItem(
                text = "Historial",
                icon = Icons.Default.History,
                selected = true,
                contentColor = if (isDark) Color(0xFFE9DDFF) else MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = onNavigateToIdeaList
            )
        }
    }
}

@Composable
private fun QuestionBottomNavItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    contentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
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
