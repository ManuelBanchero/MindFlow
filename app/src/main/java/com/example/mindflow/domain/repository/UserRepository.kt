package com.example.mindflow.domain.repository

import com.example.mindflow.domain.model.User
import com.example.mindflow.domain.model.param.RegistrationForm
import com.example.mindflow.domain.model.param.LoginForm

interface UserRepository {
    suspend fun createUser(registrationForm: RegistrationForm): Result<Unit>
    suspend fun validateCredentials(loginForm: LoginForm): Result<Unit>
    suspend fun getActiveSession(): User?
    suspend fun logOut(): Result<Unit>
    suspend fun subscribeToPlan(userId: Int): Result<Unit>
}