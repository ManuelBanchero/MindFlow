package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.model.User
import com.example.mindflow.domain.repository.UserRepository

class GetActiveSessionUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User? {
        return userRepository.getActiveSession()
    }
}