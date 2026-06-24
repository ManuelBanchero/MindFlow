package com.example.mindflow.ui.presentation.createidea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.service.AudioRecorder
import com.example.mindflow.domain.service.RecordingState
import com.example.mindflow.domain.usecase.CreateIdeaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateIdeaUiState(
    val recordingState: RecordingState = RecordingState.Idle,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val createdIdeaId: Int? = null
)

sealed interface CreateIdeaEvent {
    data object OnToggleRecord : CreateIdeaEvent
    data object OnTogglePause : CreateIdeaEvent
    data object OnCancelIdea : CreateIdeaEvent
}

@HiltViewModel
class CreateIdeaViewModel @Inject constructor(
    private val createIdeaUseCase: CreateIdeaUseCase,
    private val audioRecorder: AudioRecorder // Inyectamos nuestro nuevo servicio
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateIdeaUiState())
    val uiState: StateFlow<CreateIdeaUiState> = _uiState.asStateFlow()

    init {
        // Observamos el estado del grabador y lo vinculamos a nuestra UI
        viewModelScope.launch {
            audioRecorder.recordingState.collect { state ->
                _uiState.value = _uiState.value.copy(recordingState = state)
            }
        }
    }

    fun onEvent(event: CreateIdeaEvent) {
        when (event) {
            is CreateIdeaEvent.OnToggleRecord -> handleToggleRecord()
            is CreateIdeaEvent.OnTogglePause -> handleTogglePause()
            is CreateIdeaEvent.OnCancelIdea -> handleCancelIdea()
        }
    }

    private fun handleToggleRecord() {
        viewModelScope.launch {
            val currentState = _uiState.value.recordingState

            if (currentState is RecordingState.Idle || currentState is RecordingState.Error) {
                // Empezar a grabar
                audioRecorder.startRecord()
            } else {
                // Detener grabación y procesar el audio
                audioRecorder.stopRecord().onSuccess { path ->
                    createIdea(path)
                }.onFailure {
                    _uiState.value = _uiState.value.copy(error = "Error al detener la grabación")
                }
            }
        }
    }

    private fun handleTogglePause() {
        viewModelScope.launch {
            val currentState = _uiState.value.recordingState
            if (currentState is RecordingState.Recording) {
                audioRecorder.pauseRecord()
            } else if (currentState is RecordingState.Paused) {
                audioRecorder.resumeRecord()
            }
        }
    }

    private fun handleCancelIdea() {
        viewModelScope.launch {
            audioRecorder.cancelRecord()
            _uiState.value = CreateIdeaUiState()
        }
    }

    private fun createIdea(audioUri: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isProcessing = true,
                error = null
            )

            val result = createIdeaUseCase(audioUri)
            result.onSuccess { id ->
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
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
}
