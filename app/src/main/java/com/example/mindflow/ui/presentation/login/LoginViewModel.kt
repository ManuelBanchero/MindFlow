package com.example.mindflow.ui.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.model.param.LoginForm
import com.example.mindflow.domain.usecase.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState (
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false
)

sealed interface LoginEvent {
    data class EmailChanged(val value: String): LoginEvent
    data class PasswordChanged(val value: String): LoginEvent
    object OnLoginClick: LoginEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
): ViewModel() {
    // Private state
    private val _uiState = MutableStateFlow(LoginUiState())
    // Public not mutable state
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Public methods to interact w/Views
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(email = event.value)
            }
            is LoginEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = event.value)
            }
            is LoginEvent.OnLoginClick -> {
                executeLogin()
            }
        }
    }

    private fun executeLogin() {
        val currentEmail = _uiState.value.email
        val currentPassword = _uiState.value.password

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val loginForm = LoginForm(currentEmail, currentPassword)
            // Calling domain use case
            val result = loginUserUseCase(loginForm)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoginSuccess = true
                )
                // navigate to main page
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Error desconocido"
                )
            }
        }
    }
}