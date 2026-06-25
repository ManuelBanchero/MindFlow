package com.example.mindflow.data.remote.api

import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.data.remote.dto.LoginRequest
import com.example.mindflow.data.remote.dto.RegisterRequest
import com.example.mindflow.data.remote.dto.UserDTO
import okhttp3.MultipartBody
import okhttp3.Request
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MindFlowApiService {

    @Multipart
    @POST("ideas/process")
    suspend fun processIdea(
        @Part audio: MultipartBody.Part
    ): IdeaDTO

    @DELETE("ideas/{id}")
    suspend fun deleteIdea(
        @Path("id") id: Int
    )

    @PUT("ideas/{id}")
    suspend fun updateIdea(
        @Path("id") id: Int,
        @Body request: IdeaDTO
    ): IdeaDTO

    @Multipart
    @POST("ideas/{id}/questions/{questionId}/answer")
    suspend fun answerQuestion(
        @Path("id") id: Int,
        @Path("questionId") questionId: Int,
        @Part audio: MultipartBody.Part
    ): IdeaDTO

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): UserDTO

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): UserDTO
}
