package com.example.mindflow.ui.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.model.param.RegistrationForm
import com.example.mindflow.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val mail: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegisterSuccess: Boolean = false
)

sealed interface RegisterEvent {
    data class FirstNameChanged(val value: String) : RegisterEvent
    data class LastNameChanged(val value: String) : RegisterEvent
    data class MailChanged(val value: String) : RegisterEvent
    data class PasswordChanged(val value: String) : RegisterEvent
    data object OnRegisterClick : RegisterEvent
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FirstNameChanged -> {
                _uiState.value = _uiState.value.copy(firstName = event.value)
            }
            is RegisterEvent.LastNameChanged -> {
                _uiState.value = _uiState.value.copy(lastName = event.value)
            }
            is RegisterEvent.MailChanged -> {
                _uiState.value = _uiState.value.copy(mail = event.value)
            }
            is RegisterEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = event.value)
            }
            is RegisterEvent.OnRegisterClick -> executeRegister()
        }
    }

    private fun executeRegister() {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)

            val registrationForm = RegistrationForm(
                firstName = currentState.firstName,
                lastName = currentState.lastName,
                mail = currentState.mail,
                password = currentState.password
            )

            val result = registerUserUseCase(registrationForm)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegisterSuccess = true
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Error desconocido"
                )
            }
        }
    }
}
