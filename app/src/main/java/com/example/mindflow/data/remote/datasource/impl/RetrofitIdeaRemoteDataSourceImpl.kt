package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.api.MindFlowApiService
import com.example.mindflow.data.remote.datasource.IdeaRemoteDataSource
import com.example.mindflow.data.remote.dto.IdeaDTO
import javax.inject.Inject

class RetrofitIdeaRemoteDataSourceImpl @Inject constructor(
    private val apiService: MindFlowApiService
): IdeaRemoteDataSource {
    override suspend fun saveIdea(
        ideaDTO: IdeaDTO,
        userId: Int
    ): IdeaDTO {
        TODO("Not yet implemented")
    }

    override suspend fun updateIdea(ideaDTO: IdeaDTO) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteIdea(ideaId: Int) {
        return apiService.deleteIdea(ideaId)
    }
}