package com.example.mindflow.data.remote.api

import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MindFlowApiService {

    @Multipart
    @POST("ideas/process")
    suspend fun processIdea(
        @Part audio: MultipartBody.Part
    ): ProcessedIdeaDraftDTO
}