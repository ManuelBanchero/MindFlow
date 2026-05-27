package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.repository.IdeaRepository

class UpdateIdeaUseCase(
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(idea: Idea): Result<Unit> {
        return ideaRepository.updateIdea(idea)
    }
}