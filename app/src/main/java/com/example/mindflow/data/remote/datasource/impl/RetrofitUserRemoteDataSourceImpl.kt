package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.api.MindFlowApiService
import com.example.mindflow.data.remote.datasource.UserRemoteDataSource
import com.example.mindflow.data.remote.dto.LoginRequest
import com.example.mindflow.data.remote.dto.RegisterRequest
import com.example.mindflow.data.remote.dto.UserDTO
import javax.inject.Inject

class RetrofitUserRemoteDataSourceImpl @Inject constructor(
    private val apiService: MindFlowApiService
): UserRemoteDataSource {
    override suspend fun register(registerRequest: RegisterRequest): UserDTO {
        return apiService.register(registerRequest)
    }

    override suspend fun logIn(loginRequest: LoginRequest): UserDTO {
        return apiService.login(loginRequest)
    }

    override suspend fun subscribeToPlan(userId: Int) {
        TODO("Not yet implemented")
    }
}