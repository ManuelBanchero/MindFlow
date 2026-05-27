package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.repository.UserRepository

class LogoutUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return userRepository.logOut()
    }
}