package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.User
import com.example.mindflow.domain.repository.UserRepository
import javax.inject.Inject

class SubscribeUserToPlanUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        val user: User = userRepository.getActiveSession() ?: return Result.failure(Exception("Un error ocurrió tratando de obtener al usuario"))

        return userRepository.subscribeToPlan(user.id)
    }
}