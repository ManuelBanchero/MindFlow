package com.example.mindflow.ui.presentation.idealist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun IdeaListScreen(
    onNavigateToIdeaDetail: (Int) -> Unit,
    onNavigateToCreateIdea: () -> Unit,
    onNavigateToLogout: () -> Unit,
    viewModel: IdeaListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.selectedIdeaId) {
        val id = uiState.selectedIdeaId
        if (id != null) {
            onNavigateToIdeaDetail(id)
            viewModel.onNavigationHandled()
        }
    }

    // Llamada al componente externo
    IdeaListContent(
        uiState = uiState,
        onNavigateToCreateIdea = onNavigateToCreateIdea,
        onNavigateToLogout = onNavigateToLogout,
        onIdeaClick = { id ->
            viewModel.onEvent(IdeaListEvent.OnIdeaClick(id))
        }
    )
}
