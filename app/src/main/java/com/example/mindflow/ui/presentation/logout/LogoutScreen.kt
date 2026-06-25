package com.example.mindflow.ui.presentation.logout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collect

@Composable
fun LogoutScreen(
    onBack: () -> Unit,
    onLogoutSuccess: () -> Unit,
    viewModel: LogoutViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LogoutEffect.LoggedOut -> onLogoutSuccess()
            }
        }
    }

    LogoutContent(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                LogoutEvent.OnBackClick -> onBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}
