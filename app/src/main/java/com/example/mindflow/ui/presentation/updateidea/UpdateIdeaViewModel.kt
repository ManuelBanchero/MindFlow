package com.example.mindflow.ui.presentation.updateidea

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.StructuredSection
import com.example.mindflow.domain.model.StructuredSectionType
import com.example.mindflow.domain.usecase.GetIdeaByIdUseCase
import com.example.mindflow.domain.usecase.UpdateIdeaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class UpdateIdeaSectionUiState(
    val type: StructuredSectionType = StructuredSectionType.OTHER,
    val title: String = "",
    val content: String = ""
)

data class UpdateIdeaUiState(
    val idea: Idea? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
    val title: String = "",
    val categories: List<String> = emptyList(),
    val categoryInput: String = "",
    val summary: String = "",
    val sections: List<UpdateIdeaSectionUiState> = listOf(UpdateIdeaSectionUiState()),
    val hasInitializedForm: Boolean = false
)

sealed interface UpdateIdeaEvent {
    data object OnDiscardClick : UpdateIdeaEvent
    data object OnSaveClick : UpdateIdeaEvent
    data class OnTitleChange(val value: String) : UpdateIdeaEvent
    data class OnCategoryInputChange(val value: String) : UpdateIdeaEvent
    data object OnAddCategoryClick : UpdateIdeaEvent
    data class OnRemoveCategoryClick(val index: Int) : UpdateIdeaEvent
    data class OnSummaryChange(val value: String) : UpdateIdeaEvent
    data object OnAddSectionClick : UpdateIdeaEvent
    data class OnRemoveSectionClick(val index: Int) : UpdateIdeaEvent
    data class OnSectionTypeChange(
        val index: Int,
        val type: StructuredSectionType
    ) : UpdateIdeaEvent
    data class OnSectionTitleChange(
        val index: Int,
        val value: String
    ) : UpdateIdeaEvent
    data class OnSectionContentChange(
        val index: Int,
        val value: String
    ) : UpdateIdeaEvent
}

sealed interface UpdateIdeaEffect {
    data object Saved : UpdateIdeaEffect
}

@HiltViewModel
class UpdateIdeaViewModel @Inject constructor(
    private val getIdeaByIdUseCase: GetIdeaByIdUseCase,
    private val updateIdeaUseCase: UpdateIdeaUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ideaId: Int = checkNotNull(savedStateHandle["ideaId"])

    private val _uiState = MutableStateFlow(UpdateIdeaUiState())
    val uiState: StateFlow<UpdateIdeaUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<UpdateIdeaEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    init {
        loadIdea()
    }

    fun onEvent(event: UpdateIdeaEvent) {
        when (event) {
            UpdateIdeaEvent.OnDiscardClick -> Unit
            UpdateIdeaEvent.OnSaveClick -> saveIdea()
            is UpdateIdeaEvent.OnTitleChange -> updateTitle(event.value)
            is UpdateIdeaEvent.OnCategoryInputChange -> updateCategoryInput(event.value)
            UpdateIdeaEvent.OnAddCategoryClick -> addCategory()
            is UpdateIdeaEvent.OnRemoveCategoryClick -> removeCategory(event.index)
            is UpdateIdeaEvent.OnSummaryChange -> updateSummary(event.value)
            UpdateIdeaEvent.OnAddSectionClick -> addSection()
            is UpdateIdeaEvent.OnSectionTypeChange -> updateSectionType(event.index, event.type)
            is UpdateIdeaEvent.OnRemoveSectionClick -> removeSection(event.index)
            is UpdateIdeaEvent.OnSectionTitleChange -> updateSectionTitle(event.index, event.value)
            is UpdateIdeaEvent.OnSectionContentChange -> updateSectionContent(event.index, event.value)
        }
    }

    private fun loadIdea() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getIdeaByIdUseCase(ideaId).collectLatest { idea ->
                if (idea == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No pudimos encontrar la idea para editar."
                        )
                    }
                    return@collectLatest
                }

                _uiState.update { current ->
                    if (!current.hasInitializedForm) {
                        current.copy(
                            idea = idea,
                            isLoading = false,
                            error = null,
                            title = idea.title,
                            categories = idea.categories,
                            categoryInput = "",
                            summary = idea.summarizeContent,
                            sections = if (idea.structuredIdea.isNotEmpty()) {
                                idea.structuredIdea.map { it.toEditableSection() }
                            } else {
                                listOf(UpdateIdeaSectionUiState())
                            },
                            hasInitializedForm = true
                        )
                    } else {
                        current.copy(
                            idea = idea,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }

    private fun updateTitle(value: String) {
        _uiState.update { it.copy(title = value, error = null) }
    }

    private fun updateCategoryInput(value: String) {
        _uiState.update { it.copy(categoryInput = value, error = null) }
    }

    private fun addCategory() {
        _uiState.update { current ->
            val candidate = current.categoryInput.trim()
            val validationError = validateCategory(candidate, current.categories)
            if (validationError != null) {
                return@update current.copy(error = validationError)
            }

            current.copy(
                categories = current.categories + candidate,
                categoryInput = "",
                error = null
            )
        }
    }

    private fun removeCategory(index: Int) {
        _uiState.update { current ->
            if (index !in current.categories.indices) return@update current
            current.copy(
                categories = current.categories.toMutableList().apply { removeAt(index) },
                error = null
            )
        }
    }

    private fun updateSummary(value: String) {
        _uiState.update { it.copy(summary = value.take(180), error = null) }
    }

    private fun addSection() {
        _uiState.update { current ->
            current.copy(
                sections = current.sections + UpdateIdeaSectionUiState(),
                error = null
            )
        }
    }

    private fun removeSection(index: Int) {
        _uiState.update { current ->
            if (index !in current.sections.indices) return@update current
            current.copy(
                sections = current.sections.toMutableList().apply { removeAt(index) },
                error = null
            )
        }
    }

    private fun updateSectionType(index: Int, type: StructuredSectionType) {
        _uiState.update { current ->
            if (index !in current.sections.indices) return@update current
            val updatedSections = current.sections.toMutableList()
            updatedSections[index] = updatedSections[index].copy(type = type)
            current.copy(sections = updatedSections, error = null)
        }
    }

    private fun updateSectionTitle(index: Int, value: String) {
        _uiState.update { current ->
            if (index !in current.sections.indices) return@update current
            val updatedSections = current.sections.toMutableList()
            updatedSections[index] = updatedSections[index].copy(title = value)
            current.copy(sections = updatedSections, error = null)
        }
    }

    private fun updateSectionContent(index: Int, value: String) {
        _uiState.update { current ->
            if (index !in current.sections.indices) return@update current
            val updatedSections = current.sections.toMutableList()
            updatedSections[index] = updatedSections[index].copy(content = value)
            current.copy(sections = updatedSections, error = null)
        }
    }

    private fun saveIdea() {
        val current = uiState.value
        val idea = current.idea ?: return

        val validationError = validateForm(current)
        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        val updatedIdea = idea.copy(
            title = current.title.trim(),
            categories = current.categories
                .map { it.trim() }
                .filter { it.isNotBlank() },
            summarizeContent = current.summary.trim(),
            structuredIdea = current.sections.map { section ->
                StructuredSection(
                    type = section.type,
                    title = section.title.trim(),
                    content = section.content.trim()
                )
            },
            updatedAt = Instant.now()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val result = updateIdeaUseCase(updatedIdea)
            if (result.isSuccess) {
                _uiState.update { it.copy(isSaving = false) }
                _effects.emit(UpdateIdeaEffect.Saved)
            } else {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = result.exceptionOrNull()?.message ?: "No se pudo guardar la idea"
                    )
                }
            }
        }
    }

    private fun validateForm(state: UpdateIdeaUiState): String? {
        if (state.title.trim().isBlank()) return "El título no puede estar vacío."
        if (state.categories.isEmpty()) return "Agregá al menos una categoría."
        if (state.summary.trim().isBlank()) return "El resumen no puede estar vacío."
        if (state.summary.length > 180) return "El resumen no puede superar los 180 caracteres."
        if (state.categories.size > 3) return "La idea puede tener como máximo 3 categorías."

        state.categories.forEach { category ->
            val categoryError = validateCategory(category, state.categories, allowExisting = true)
            if (categoryError != null) return categoryError
        }

        state.sections.forEachIndexed { index, section ->
            if (section.title.trim().isBlank()) {
                return "La sección ${index + 1} necesita un título."
            }
            if (section.content.trim().isBlank()) {
                return "La sección ${index + 1} no puede quedar vacía."
            }
        }

        return null
    }

    private fun validateCategory(
        candidate: String,
        existingCategories: List<String>,
        allowExisting: Boolean = false
    ): String? {
        if (candidate.isBlank()) return "La categoría no puede estar vacía."
        if (candidate.length > 20) return "La categoría no puede superar los 20 caracteres."
        if (candidate.split(Regex("\\s+")).filter { it.isNotBlank() }.size > 2) {
            return "La categoría puede tener hasta dos palabras."
        }
        if (!allowExisting && existingCategories.size >= 3) return "Solo podés tener hasta 3 categorías."
        if (!allowExisting && existingCategories.any { it.equals(candidate, ignoreCase = true) }) {
            return "Esa categoría ya fue agregada."
        }
        return null
    }

    private fun StructuredSection.toEditableSection(): UpdateIdeaSectionUiState {
        return UpdateIdeaSectionUiState(
            type = StructuredSectionType.entries.firstOrNull { it.displayName().equals(title.trim(), ignoreCase = true) }
                ?: type,
            title = title,
            content = content
        )
    }

    private fun StructuredSectionType.displayName(): String {
        return name.lowercase()
            .split('_')
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }
}
