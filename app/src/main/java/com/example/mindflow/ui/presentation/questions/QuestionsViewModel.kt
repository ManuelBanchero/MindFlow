package com.example.mindflow.ui.presentation.questions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.service.AudioRecorder
import com.example.mindflow.domain.service.RecordingState
import com.example.mindflow.domain.usecase.AnswerQuestionUseCase
import com.example.mindflow.domain.usecase.GetIdeaByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestionsUiState(
    val idea: Idea? = null,
    val recordingState: RecordingState = RecordingState.Idle,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val activeQuestionId: Int? = null,
    val error: String? = null
)

sealed interface QuestionsEvent {
    data class OnToggleRecord(val questionId: Int) : QuestionsEvent
    data class OnTogglePause(val questionId: Int) : QuestionsEvent
    data class OnCancelRecord(val questionId: Int) : QuestionsEvent
    data object OnDismissError : QuestionsEvent
}

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val getIdeaByIdUseCase: GetIdeaByIdUseCase,
    private val answerQuestionUseCase: AnswerQuestionUseCase,
    private val audioRecorder: AudioRecorder,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ideaId: Int = checkNotNull(savedStateHandle["ideaId"])

    private val _uiState = MutableStateFlow(QuestionsUiState())
    val uiState: StateFlow<QuestionsUiState> = _uiState.asStateFlow()

    init {
        observeRecorderState()
        observeIdea()
    }

    fun onEvent(event: QuestionsEvent) {
        when (event) {
            is QuestionsEvent.OnToggleRecord -> handleToggleRecord(event.questionId)
            is QuestionsEvent.OnTogglePause -> handleTogglePause(event.questionId)
            is QuestionsEvent.OnCancelRecord -> handleCancelRecord(event.questionId)
            QuestionsEvent.OnDismissError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun observeRecorderState() {
        viewModelScope.launch {
            audioRecorder.recordingState.collectLatest { state ->
                _uiState.update {
                    it.copy(
                        recordingState = state,
                        isSubmitting = if (state is RecordingState.Error) false else it.isSubmitting,
                        activeQuestionId = if (state is RecordingState.Error) null else it.activeQuestionId,
                        error = if (state is RecordingState.Error) state.message else it.error
                    )
                }
            }
        }
    }

    private fun observeIdea() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getIdeaByIdUseCase(ideaId).collectLatest { idea ->
                _uiState.update {
                    it.copy(
                        idea = idea,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun handleToggleRecord(questionId: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value.recordingState
            val currentQuestion = _uiState.value.idea?.questions?.firstOrNull { it.id == questionId }
            val activeQuestionId = _uiState.value.activeQuestionId

            if (currentQuestion == null) {
                _uiState.update { it.copy(error = "No pudimos encontrar esta pregunta.") }
                return@launch
            }

            if (currentQuestion.wasAnswered) {
                return@launch
            }

            if (activeQuestionId != null && activeQuestionId != questionId && currentState !is RecordingState.Idle && currentState !is RecordingState.Error) {
                _uiState.update { it.copy(error = "Terminá o cancelá la respuesta actual antes de pasar a otra pregunta.") }
                return@launch
            }

            if (currentState is RecordingState.Idle || currentState is RecordingState.Error) {
                _uiState.update { it.copy(activeQuestionId = questionId, error = null) }
                audioRecorder.startRecord().onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            activeQuestionId = null,
                            error = exception.message ?: "No se pudo iniciar la grabación."
                        )
                    }
                }
            } else if (activeQuestionId == questionId) {
                audioRecorder.stopRecord().onSuccess { path ->
                    submitAnswer(questionId, path)
                }.onFailure {
                    _uiState.update {
                        it.copy(
                            activeQuestionId = null,
                            error = "Error al detener la grabación."
                        )
                    }
                }
            }
        }
    }

    private fun handleTogglePause(questionId: Int) {
        viewModelScope.launch {
            if (_uiState.value.activeQuestionId != questionId) return@launch

            when (_uiState.value.recordingState) {
                RecordingState.Recording -> audioRecorder.pauseRecord()
                RecordingState.Paused -> audioRecorder.resumeRecord()
                else -> Unit
            }
        }
    }

    private fun handleCancelRecord(questionId: Int) {
        viewModelScope.launch {
            if (_uiState.value.activeQuestionId != questionId) return@launch
            audioRecorder.cancelRecord()
            _uiState.update {
                it.copy(
                    activeQuestionId = null,
                    isSubmitting = false,
                    error = null
                )
            }
        }
    }

    private fun submitAnswer(questionId: Int, audioPath: String) {
        val currentIdea = _uiState.value.idea ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }

            answerQuestionUseCase(currentIdea, questionId, audioPath)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            activeQuestionId = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            activeQuestionId = null,
                            error = exception.message ?: "No pudimos procesar esta respuesta."
                        )
                    }
                }
        }
    }
}
