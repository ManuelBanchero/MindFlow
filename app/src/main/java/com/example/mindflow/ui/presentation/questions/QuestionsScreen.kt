package com.example.mindflow.ui.presentation.questions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mindflow.ui.components.RecordingLifecycleEffect

@Composable
fun QuestionsScreen(
    onBack: () -> Unit,
    onNavigateToCreateIdea: () -> Unit,
    onNavigateToIdeaList: () -> Unit,
    viewModel: QuestionsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecordingLifecycleEffect(
        onCleanup = viewModel::onScreenLifecycleEnded
    )

    QuestionsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToCreateIdea = onNavigateToCreateIdea,
        onNavigateToIdeaList = onNavigateToIdeaList
    )
}
