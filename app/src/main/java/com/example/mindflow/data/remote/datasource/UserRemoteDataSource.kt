package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.RegisterRequest
import com.example.mindflow.data.remote.dto.LoginRequest
import com.example.mindflow.data.remote.dto.UserDTO

interface UserRemoteDataSource {
    suspend fun register(registerRequest: RegisterRequest): UserDTO
    suspend fun logIn(loginRequest: LoginRequest): UserDTO
    suspend fun subscribeToPlan(userId: Int)
}