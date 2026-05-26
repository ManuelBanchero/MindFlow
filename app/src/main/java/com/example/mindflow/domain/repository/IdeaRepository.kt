package com.example.mindflow.domain.repository

import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.ProcessedIdeaDraft
import kotlinx.coroutines.flow.Flow

interface IdeaRepository {
    suspend fun processIdea(audioFilePath: String): Result<ProcessedIdeaDraft>
    suspend fun saveIdea(processedIdea: ProcessedIdeaDraft, userId: Int): Result<Int> // Returns the new Idea id (the app need it to navigate to the Idea detail page)
    suspend fun updateIdea(idea: Idea): Result<Unit>
    suspend fun deleteIdea(idea: Idea): Result<Unit>
    suspend fun expandIdea(idea: Idea, newIdeaContent: String): Result<Unit>
    suspend fun answerQuestion(
        idea: Idea,
        questionId: Int,
        responseContent: String
    ): Result<Unit>

    fun getIdeasFlow(userId: Int): Flow<List<Idea>>
    fun getIdeaById(ideaId: Int, userId: Int): Flow<Idea?>
}