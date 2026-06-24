package com.example.mindflow.data.remote.api

import com.example.mindflow.data.remote.dto.IdeaDTO
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
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
}
