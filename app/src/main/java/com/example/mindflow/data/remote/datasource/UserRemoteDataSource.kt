package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.LoginRequest
import com.example.mindflow.data.remote.dto.SignInRequest
import com.example.mindflow.data.remote.dto.UserDTO

interface UserRemoteDataSource {
    suspend fun logIn(loginRequest: LoginRequest): UserDTO

    suspend fun signIn(signInRequest: SignInRequest): UserDTO

    suspend fun subscribeToPlan(userId: Int)
}