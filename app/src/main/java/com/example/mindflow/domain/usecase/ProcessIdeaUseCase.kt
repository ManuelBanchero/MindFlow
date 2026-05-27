package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.repository.IdeaRepository
import com.example.mindflow.domain.repository.UserRepository

class CreateIdeaUseCase(
    private val userRepository: UserRepository,
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(audioUri: String): Result<Int> {
        if (audioUri.isBlank())
            return Result.failure(Exception("La dirección de archivo de audio no es válida"))

        val processedIdea = ideaRepository.processIdea(audioUri).getOrElse {
            return Result.failure(Exception("Ocurrió un error al intentar procesar la idea"))
        }
        val user = userRepository.getActiveSession()?: return Result.failure(Exception("Ocurrió un error al intentar obtener el usuario"))
        return ideaRepository.saveIdea(processedIdea, user.id)
    }
}