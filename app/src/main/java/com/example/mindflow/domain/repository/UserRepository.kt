package com.example.mindflow.domain.repository

import com.example.mindflow.domain.model.User

interface UserRepository {
    suspend fun createUser(
        firstName: String,
        lastName: String,
        mail: String,
        password: String
    ): Result<Unit>

    suspend fun logIn(
        mail: String,
        password: String
    ): Result<User>

    suspend fun setActiveSession(user: User): Result<Unit>

    suspend fun getActiveSession(): User?

    suspend fun logOut(userId: Int): Result<Unit>

    suspend fun subscribeToPlan(userId: Int): Result<Unit>
}