package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.datasource.IdeaRemoteDataSource
import com.example.mindflow.data.remote.dto.IdeaDTO
import javax.inject.Inject

class MockIdeaRemoteDataSource @Inject constructor(): IdeaRemoteDataSource {
    override suspend fun saveIdea(
        ideaDTO: IdeaDTO,
        userId: Int
    ): IdeaDTO {
        return ideaDTO.copy(userId = userId)
    }

    override suspend fun updateIdea(ideaDTO: IdeaDTO): IdeaDTO {
        return ideaDTO
    }

    override suspend fun deleteIdea(userId: Int) {
        return
    }

    override suspend fun getUserIdeas(userId: Int): List<IdeaDTO> {
        return emptyList()
    }
}
