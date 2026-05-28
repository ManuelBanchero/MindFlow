package com.example.mindflow.ui.presentation.idealist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.usecase.GetIdeasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class IdeaListUiState (
    val ideas: List<Idea> = listOf<Idea>(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedIdeaId: Int? = null
)

sealed interface IdeaListEvent {
    data class OnIdeaClick(val id: Int): IdeaListEvent
}

@HiltViewModel
class IdeaListViewModel @Inject constructor(
    private val getIdeasUseCase: GetIdeasUseCase
): ViewModel() {
    // Private state
    private val _uiState = MutableStateFlow(IdeaListUiState())
    // Public state
    val uiState: StateFlow<IdeaListUiState> = _uiState.asStateFlow()

    init {
        loadIdeas()
    }

    // Public methods to interact w/view
    fun onEvent(event: IdeaListEvent) {
        when (event) {
            is IdeaListEvent.OnIdeaClick -> {
                _uiState.value = _uiState.value.copy(selectedIdeaId = event.id)
            }
        }
    }

    // reset idea id for navigation porpoises
    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(selectedIdeaId = null)
    }
    private fun loadIdeas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                getIdeasUseCase().collect { ideaList ->
                    _uiState.value = _uiState.value.copy(
                        ideas = ideaList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar las ideas"
                )
            }
        }
    }
}