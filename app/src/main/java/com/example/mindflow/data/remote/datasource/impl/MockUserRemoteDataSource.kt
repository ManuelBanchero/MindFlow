package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.datasource.UserRemoteDataSource
import com.example.mindflow.data.remote.dto.RegisterRequest
import com.example.mindflow.data.remote.dto.LoginRequest
import com.example.mindflow.data.remote.dto.UserDTO
import javax.inject.Inject

class MockUserRemoteDataSource @Inject constructor(): UserRemoteDataSource {
    override suspend fun register(registerRequest: RegisterRequest): UserDTO {
        return UserDTO(
            id = 1,
            firstName = "Manuel",
            lastName = "Banchero",
            mail = "manuelbanchero@gmail.com",
            isSubscribed = false
        )
    }

    override suspend fun logIn(loginRequest: LoginRequest): UserDTO {
        return UserDTO(
            id = 1,
            firstName = "Manuel",
            lastName = "Banchero",
            mail = "manuelbanchero@gmail.com",
            isSubscribed = false
        )
    }

    override suspend fun subscribeToPlan(userId: Int) {
        return
    }
}
