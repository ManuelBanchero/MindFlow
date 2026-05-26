package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.LoginRequest

interface UserRemoteDataSource {
    suspend fun logIn(loginRequest: LoginRequest)

    suspend fun signIn(
        mail: String,
        password: String
    )
}