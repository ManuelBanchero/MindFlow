package com.example.mindflow.domain.repository

import android.net.Uri
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.ProcessedIdeaResult
import kotlinx.coroutines.flow.Flow

interface IdeaRepository {
    suspend fun processIdea(audioUri: Uri): Result<ProcessedIdeaResult>
    suspend fun saveIdea(processedIdea: ProcessedIdeaResult, userId: Int): Result<Int> // Returns the new Idea id (the app need it to navigate to the Idea detail page)
    suspend fun updateIdea(idea: Idea): Result<Unit>
    suspend fun deleteIdea(idea: Idea): Result<Unit>
    suspend fun expandIdea(idea: Idea, audioUri: Uri): Result<Unit>
    suspend fun answerQuestion(
        idea: Idea,
        questionId: Int,
        audioUri: Uri
    ): Result<Unit>

    fun getIdeasFlow(userId: Int): Flow<List<Idea>>
    fun getIdeaById(ideaId: Int, userId: Int): Flow<Idea?>
}