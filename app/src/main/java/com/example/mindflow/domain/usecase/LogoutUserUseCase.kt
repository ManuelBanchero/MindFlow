package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return userRepository.logOut()
    }
}