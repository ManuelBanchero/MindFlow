package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.repository.IdeaRepository

class AnswerQuestionUserCase(
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(
        idea: Idea,
        questionId: Int,
        audioUri: String
    ): Result<Unit> {
        if (audioUri.isBlank())
            return Result.failure(Exception("La dirección del archivo de audio no es valída"))

        return ideaRepository.answerQuestion(idea, questionId, audioUri)
    }
}