package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.domain.model.ProcessedIdeaDraft

interface IdeaRemoteDataSource {
    suspend fun saveIdea(processedIdeaDraft: ProcessedIdeaDraft, userId: Int): IdeaDTO
}