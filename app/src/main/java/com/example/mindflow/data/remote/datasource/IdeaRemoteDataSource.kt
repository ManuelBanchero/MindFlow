package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO

interface IdeaRemoteDataSource {
    suspend fun saveIdea(
        processedIdeaDraftDTO: ProcessedIdeaDraftDTO,
        audioTranscribed: String,
        userId: Int
    ): IdeaDTO
    suspend fun updateIdea(ideaDTO: IdeaDTO)
    suspend fun deleteIdea(userId: Int)
}