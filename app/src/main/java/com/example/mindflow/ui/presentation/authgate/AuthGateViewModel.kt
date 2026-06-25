package com.example.mindflow.ui.presentation.authgate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.usecase.GetActiveSessionUseCase
import com.example.mindflow.domain.usecase.IsSessionActiveUseCase
import com.example.mindflow.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthGateUiState(
    val isLoading: Boolean = true,
    val targetRoute: String? = null
)

@HiltViewModel
class AuthGateViewModel @Inject constructor(
    private val isSessionActiveUseCase: IsSessionActiveUseCase,
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthGateUiState())
    val uiState: StateFlow<AuthGateUiState> = _uiState.asStateFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        viewModelScope.launch {
            val hasActiveSession = isSessionActiveUseCase()

            if (!hasActiveSession) {
                _uiState.value = AuthGateUiState(
                    isLoading = false,
                    targetRoute = "login"
                )
                return@launch
            }

            val activeUser = getActiveSessionUseCase()
            if (activeUser != null) {
                _uiState.value = AuthGateUiState(
                    isLoading = false,
                    targetRoute = "create_idea"
                )
            } else {
                logoutUserUseCase()
                _uiState.value = AuthGateUiState(
                    isLoading = false,
                    targetRoute = "login"
                )
            }
        }
    }
}
