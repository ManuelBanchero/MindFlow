package com.example.mindflow.data.repository

import com.example.mindflow.data.local.room.dao.IdeaDAO
import com.example.mindflow.data.mapper.toEntity
import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.datasource.IdeaRemoteDataSource
import com.example.mindflow.data.remote.datasource.SpeechToTextDataSource
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.ProcessedIdeaDraft
import com.example.mindflow.domain.repository.IdeaRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class IdeaRepositoryImp(
   private val ideaDAO: IdeaDAO,
   private val ideaRemoteDataSource: IdeaRemoteDataSource,
   private val speechToTextDataSource: SpeechToTextDataSource,
   private val ideaProcessorDataSource: IdeaProcessorDataSource
): IdeaRepository {
    override suspend fun processIdea(audioFilePath: String): Result<ProcessedIdeaDraft> {
        return try {
            val audioContent: String = speechToTextDataSource.transcribeAudio(audioFilePath)
            val processedIdea = ideaProcessorDataSource.processRawText(audioContent)

            Result.success(processedIdea)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIdea(processedIdea: ProcessedIdeaDraft, userId: Int): Result<Int> {
        return try {
            // Save it remotely
            val idea: IdeaDTO = ideaRemoteDataSource.saveIdea(processedIdea, userId)
            // Save it locally
            val ideaEntity = idea.toEntity()
            val questionsEntity = idea.questions.map({ it.toEntity() })
            ideaDAO.insertIdea(ideaEntity)
            ideaDAO.insertQuestions(questionsEntity)

            Result.success(idea.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateIdea(idea: Idea): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteIdea(idea: Idea): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun expandIdea(
        idea: Idea,
        newIdeaContent: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun answerQuestion(
        idea: Idea,
        questionId: Int,
        responseContent: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getIdeasFlow(userId: Int): Flow<List<Idea>> {
        TODO("Not yet implemented")
    }

    override fun getIdeaById(
        ideaId: Int,
        userId: Int
    ): Flow<Idea?> {
        TODO("Not yet implemented")
    }
}