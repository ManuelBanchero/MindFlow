package com.example.mindflow.ui.presentation.ideadetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.usecase.DeleteIdeaUseCase
import com.example.mindflow.domain.usecase.GetIdeaByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class IdeaDetailUiState(
    val idea: Idea? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteDialog: Boolean = false
)

sealed interface IdeaDetailEvent {
    data object OnEditClick : IdeaDetailEvent
    data object OnDeleteClick : IdeaDetailEvent
    data object OnConfirmDelete : IdeaDetailEvent
    data object OnDismissDelete : IdeaDetailEvent
    data object OnQuestionsClick : IdeaDetailEvent
}

sealed interface IdeaDetailEffect {
    data class NavigateToEdit(val id: Int) : IdeaDetailEffect
    data class NavigateToQuestions(val id: Int) : IdeaDetailEffect
    data object Deleted : IdeaDetailEffect
}

@HiltViewModel
class IdeaDetailViewModel @Inject constructor(
    private val getIdeaByIdUseCase: GetIdeaByIdUseCase,
    private val deleteIdeaUseCase: DeleteIdeaUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ideaId: Int = checkNotNull(savedStateHandle["ideaId"])

    private val _uiState = MutableStateFlow(IdeaDetailUiState())
    val uiState: StateFlow<IdeaDetailUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<IdeaDetailEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    init {
        loadIdea()
    }

    fun onEvent(event: IdeaDetailEvent) {
        when (event) {
            IdeaDetailEvent.OnEditClick -> {
                viewModelScope.launch { _effects.emit(IdeaDetailEffect.NavigateToEdit(ideaId)) }
            }
            IdeaDetailEvent.OnDeleteClick -> {
                _uiState.update { it.copy(showDeleteDialog = true) }
            }
            IdeaDetailEvent.OnConfirmDelete -> {
                deleteIdea()
            }
            IdeaDetailEvent.OnDismissDelete -> {
                _uiState.update { it.copy(showDeleteDialog = false) }
            }
            IdeaDetailEvent.OnQuestionsClick -> {
                viewModelScope.launch { _effects.emit(IdeaDetailEffect.NavigateToQuestions(ideaId)) }
            }
        }
    }

    private fun loadIdea() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                getIdeaByIdUseCase(ideaId).collect { idea ->
                    _uiState.update { 
                        it.copy(
                            idea = idea,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar la idea"
                    )
                }
            }
        }
    }

    private fun deleteIdea() {
        val currentIdea = uiState.value.idea ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showDeleteDialog = false) }
            val result = deleteIdeaUseCase(currentIdea)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                _effects.emit(IdeaDetailEffect.Deleted)
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error al eliminar la idea"
                    )
                }
            }
        }
    }
}
