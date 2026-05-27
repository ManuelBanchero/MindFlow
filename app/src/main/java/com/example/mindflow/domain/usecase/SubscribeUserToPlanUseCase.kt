package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.User
import com.example.mindflow.domain.repository.UserRepository

class SubscribeUserToPlanUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        val user: User = userRepository.getActiveSession() ?: return Result.failure(Exception("Un error ocurrió tratando de obtener al usuario"))

        return userRepository.subscribeToPlan(user.id)
    }
}