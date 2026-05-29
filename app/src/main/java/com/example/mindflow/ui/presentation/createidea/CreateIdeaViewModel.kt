package com.example.mindflow.ui.presentation.createidea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.usecase.CreateIdeaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateIdeaUiState(
    val isRecording: Boolean = false,
    val isPaused: Boolean = false,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val createdIdeaId: Int? = null
)

sealed interface CreateIdeaEvent {
    data class OnToggleRecord(val audioUri: String? = null): CreateIdeaEvent
    data object OnTogglePause: CreateIdeaEvent
    data object OnCancelIdea: CreateIdeaEvent
}

@HiltViewModel
class CreateIdeaViewModel @Inject constructor(
    private val createIdeaUseCase: CreateIdeaUseCase
): ViewModel() {
    // Private state
    private val _uiState = MutableStateFlow(CreateIdeaUiState())
    // Public state
    val uiState: StateFlow<CreateIdeaUiState> = _uiState.asStateFlow()

    // Public methods to interact w/view
    fun onEvent(event: CreateIdeaEvent) {
        when (event) {
            is CreateIdeaEvent.OnToggleRecord -> {
                if (_uiState.value.isRecording) {
                    // If it was recording and clicked the button again -> process idea
                    val audioUri = event.audioUri
                    createIdea(audioUri)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isRecording = true
                    )
                }
            }
            is CreateIdeaEvent.OnTogglePause -> {
                _uiState.value = _uiState.value.copy(
                    isPaused = !_uiState.value.isPaused
                )
            }
            is CreateIdeaEvent.OnCancelIdea -> {
                _uiState.value = _uiState.value.copy(
                    isRecording = false
                )
                cancelIdea()
            }
        }
    }

    private fun createIdea(audioUri: String?) {
        if (audioUri == null) {
            _uiState.value = _uiState.value.copy(
                isRecording = false,
                error = "No se pudo obtener el audio de la grabación"
            )

            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRecording = false,
                isProcessing = true,
                error = null
            )

            val result = createIdeaUseCase(audioUri)
            result.onSuccess { id ->
                _uiState.value = _uiState.value.copy(
                    createdIdeaId = id
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    error = exception.message ?: "Error desconocido creando la idea"
                )
            }
        }
    }

    private fun cancelIdea() {}
}