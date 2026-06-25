package com.example.mindflow.ui.presentation.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.model.User
import com.example.mindflow.domain.usecase.GetActiveSessionUseCase
import com.example.mindflow.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogoutUiState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val isLoggingOut: Boolean = false,
    val error: String? = null,
    val showConfirmDialog: Boolean = false
)

sealed interface LogoutEvent {
    data object OnBackClick : LogoutEvent
    data object OnLogoutClick : LogoutEvent
    data object OnConfirmLogout : LogoutEvent
    data object OnDismissDialog : LogoutEvent
}

sealed interface LogoutEffect {
    data object LoggedOut : LogoutEffect
}

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogoutUiState())
    val uiState: StateFlow<LogoutUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LogoutEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    init {
        loadUser()
    }

    fun onEvent(event: LogoutEvent) {
        when (event) {
            LogoutEvent.OnBackClick -> Unit
            LogoutEvent.OnLogoutClick -> _uiState.update { it.copy(showConfirmDialog = true) }
            LogoutEvent.OnConfirmLogout -> logout()
            LogoutEvent.OnDismissDialog -> _uiState.update { it.copy(showConfirmDialog = false) }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = getActiveSessionUseCase()
            _uiState.value = _uiState.value.copy(
                user = user,
                isLoading = false,
                error = if (user == null) "No encontramos una sesión activa." else null
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            val currentUser = _uiState.value.user
            if (currentUser == null) {
                _uiState.update {
                    it.copy(
                        isLoggingOut = false,
                        showConfirmDialog = false,
                        error = "No encontramos una sesión activa."
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(isLoggingOut = true, error = null, showConfirmDialog = false) }

            logoutUserUseCase()
                .onSuccess {
                    _uiState.update { it.copy(isLoggingOut = false) }
                    _effects.emit(LogoutEffect.LoggedOut)
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoggingOut = false,
                            error = exception.message ?: "No pudimos cerrar la sesión."
                        )
                    }
                }
        }
    }
}
