package com.example.mindflow.ui.presentation.ideadetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IdeaDetailScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToQuestions: (Int) -> Unit,
    onDeleted: () -> Unit,
    viewModel: IdeaDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is IdeaDetailEffect.NavigateToEdit -> onNavigateToEdit(effect.id)
                is IdeaDetailEffect.NavigateToQuestions -> onNavigateToQuestions(effect.id)
                IdeaDetailEffect.Deleted -> onDeleted()
            }
        }
    }

    IdeaDetailContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToQuestions = onNavigateToQuestions
    )
}
