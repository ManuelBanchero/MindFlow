package com.example.mindflow.ui.presentation.updateidea

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UpdateIdeaScreen(
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: UpdateIdeaViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                UpdateIdeaEffect.Saved -> onSaveSuccess()
            }
        }
    }

    UpdateIdeaContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}
