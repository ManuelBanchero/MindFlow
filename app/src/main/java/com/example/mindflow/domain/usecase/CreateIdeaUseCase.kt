package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.repository.IdeaRepository
import com.example.mindflow.domain.repository.UserRepository
import javax.inject.Inject

class CreateIdeaUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(audioUri: String): Result<Int> {
        if (audioUri.isBlank())
            return Result.failure(Exception("La dirección de archivo de audio no es válida"))

        val activeUser = userRepository.getActiveSession()
            ?: return Result.failure(Exception("Ocurrió un error al intentar obtener el usuario"))

        val processedIdea = ideaRepository.processIdea(audioUri, activeUser.id).getOrElse { exception ->
            val errorMessage = exception.message ?: "Error desconocido al procesar la idea"
            return Result.failure(Exception(errorMessage))
        }
        return ideaRepository.saveIdea(processedIdea, activeUser.id)
    }
}
