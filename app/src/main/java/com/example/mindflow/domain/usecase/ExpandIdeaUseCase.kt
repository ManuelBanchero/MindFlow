package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.repository.IdeaRepository

class ExpandIdeaUseCase(
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(
        idea: Idea,
        audioUri: String
    ): Result<Unit> {
        if (audioUri.isBlank())
            return Result.failure(Exception("La dirección del archivo de audio no es valída"))

        return ideaRepository.expandIdea(idea, audioUri)
    }
}