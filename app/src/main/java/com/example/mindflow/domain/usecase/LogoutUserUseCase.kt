package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.repository.IdeaRepository
import com.example.mindflow.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        ideaRepository.deleteAllLocalIdeas()
            .onFailure { return Result.failure(it) }

        return userRepository.logOut()
    }
}
