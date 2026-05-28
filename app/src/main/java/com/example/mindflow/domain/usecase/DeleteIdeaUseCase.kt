package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.repository.IdeaRepository
import javax.inject.Inject

class DeleteIdeaUseCase @Inject constructor(
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(idea: Idea): Result<Unit> {
        return ideaRepository.deleteIdea(idea)
    }
}