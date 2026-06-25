package com.example.mindflow.ui.presentation.questions

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue

@Composable
fun QuestionsScreen(
    onBack: () -> Unit,
    onNavigateToCreateIdea: () -> Unit,
    onNavigateToIdeaList: () -> Unit,
    viewModel: QuestionsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuestionsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToCreateIdea = onNavigateToCreateIdea,
        onNavigateToIdeaList = onNavigateToIdeaList
    )
}
