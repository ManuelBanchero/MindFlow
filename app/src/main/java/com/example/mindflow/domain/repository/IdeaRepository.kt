package com.example.mindflow.domain.repository

import com.example.mindflow.domain.model.Idea
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import kotlinx.coroutines.flow.Flow

interface IdeaRepository {
    suspend fun processIdea(audioFilePath: String): Result<ProcessedIdeaDraftDTO>
    suspend fun saveIdea(processedIdea: ProcessedIdeaDraftDTO, userId: Int): Result<Int> // Returns the new Idea id (the app need it to navigate to the Idea detail page)
    suspend fun updateIdea(idea: Idea): Result<Unit>
    suspend fun deleteIdea(idea: Idea): Result<Unit>
    suspend fun expandIdea(idea: Idea, audioFilePath: String): Result<Unit>
    suspend fun answerQuestion(
        idea: Idea,
        questionId: Int,
        responseContent: String
    ): Result<Unit>

    fun getIdeasFlow(userId: Int): Flow<List<Idea>>
    fun getIdeaById(ideaId: Int, userId: Int): Flow<Idea?>
}