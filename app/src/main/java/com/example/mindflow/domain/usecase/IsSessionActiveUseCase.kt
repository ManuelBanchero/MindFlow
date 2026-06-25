package com.example.mindflow.domain.usecase

import com.example.mindflow.domain.repository.UserRepository
import javax.inject.Inject

class IsSessionActiveUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Boolean {
        return userRepository.isSessionActive()
    }
}
