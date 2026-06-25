package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.IdeaDTO

interface IdeaRemoteDataSource {
    suspend fun saveIdea(
        ideaDTO: IdeaDTO,
        userId: Int
    ): IdeaDTO
    suspend fun updateIdea(ideaDTO: IdeaDTO): IdeaDTO
    suspend fun deleteIdea(ideaId: Int)

    suspend fun getUserIdeas(userId: Int): List<IdeaDTO>
}
