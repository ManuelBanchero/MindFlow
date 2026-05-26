package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.LoginRequest
import com.example.mindflow.data.remote.dto.SignInRequest
import com.example.mindflow.data.remote.dto.UserDTO

interface UserRemoteDataSource {
    suspend fun logIn(loginRequest: LoginRequest): Result<UserDTO>

    suspend fun signIn(signInRequest: SignInRequest): Result<UserDTO>

    suspend fun subscribeToPlan(userId: Int): Result<Unit>
}