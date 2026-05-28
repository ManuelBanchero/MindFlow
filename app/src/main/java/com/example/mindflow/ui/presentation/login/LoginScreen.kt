package com.example.mindflow.ui.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle



@Composable
fun LoginScreen(
    onNavigateToMainPage: () -> Unit,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onNavigateToMainPage()
        }
    }

    LoginContent(
        uiState = uiState,
        onEvent = { event -> viewModel.onEvent(event) }
    )
}