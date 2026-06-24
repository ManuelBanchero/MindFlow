package com.example.mindflow.ui.presentation.createidea

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CreateIdeaScreen(
    onCreateIdeaSuccess: (Int) -> Unit,
    onNavigateToIdeaList: () -> Unit,
    viewModel: CreateIdeaViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.createdIdeaId) {
        val id: Int? = uiState.createdIdeaId
        if (id != null) {
            onCreateIdeaSuccess(id)
        }
    }

    CreateIdeaContent(
        uiState,
        onEvent = { event -> viewModel.onEvent(event) },
        onNavigateToIdeaList = onNavigateToIdeaList
    )
}
