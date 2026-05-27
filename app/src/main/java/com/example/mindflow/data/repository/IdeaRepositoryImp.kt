package com.example.mindflow.data.repository

import com.example.mindflow.data.local.room.dao.IdeaDAO
import com.example.mindflow.data.mapper.toDto
import com.example.mindflow.data.mapper.toEntity
import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.datasource.IdeaRemoteDataSource
import com.example.mindflow.data.remote.datasource.SpeechToTextDataSource
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.domain.repository.IdeaRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant

class IdeaRepositoryImp(
   private val ideaDao: IdeaDAO,
   private val ideaRemoteDataSource: IdeaRemoteDataSource,
   private val speechToTextDataSource: SpeechToTextDataSource,
   private val ideaProcessorDataSource: IdeaProcessorDataSource
): IdeaRepository {
    override suspend fun processIdea(audioFilePath: String): Result<ProcessedIdeaDraftDTO> {
        return try {
            val audioContent: String = speechToTextDataSource.transcribeAudio(audioFilePath)
            val processedIdea = ideaProcessorDataSource.processRawText(audioContent)

            Result.success(processedIdea)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIdea(processedIdea: ProcessedIdeaDraftDTO, userId: Int): Result<Int> {
        return try {
            // Save it remotely
            val idea: IdeaDTO = ideaRemoteDataSource.saveIdea(processedIdea, userId)
            // Save it locally
            ideaDao.insertIdea(idea.toEntity())
            ideaDao.insertQuestions(
                idea.questions.map({ it.toEntity()} )
            )

            Result.success(idea.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateIdea(idea: Idea): Result<Unit> {
        return try {
            // Update it remotely
            ideaRemoteDataSource.updateIdea(idea.toDto())
            // Update it locally
            ideaDao.insertIdea(idea.toEntity())
            ideaDao.insertQuestions(
                idea.questions.map({ it.toEntity() })
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteIdea(idea: Idea): Result<Unit> {
        return try {
            // Delete it remotely
            ideaRemoteDataSource.deleteIdea(idea.id)
            // Delete it locally
            ideaDao.deleteIdea(idea.toEntity())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun expandIdea(
        idea: Idea,
        audioFilePath: String
    ): Result<Unit> {
        return try {
            // Get new context
            val newIdeaContext = speechToTextDataSource.transcribeAudio(audioFilePath)
            // Expand idea
            val extendedIdea: ProcessedIdeaDraftDTO = ideaProcessorDataSource.expandIdeaWithNewContext(
                idea.title,
                idea.structuredIdea,
                newIdeaContext
            )
            // Create updated idea
            val updatedIdea = idea.copy(
                title = extendedIdea.title,
                updatedAt = Instant.now(),
                category = extendedIdea.category,
                textsAudiosHistory = idea.textsAudiosHistory + newIdeaContext,
                summarizeContent = extendedIdea.summarizeContent,
                structuredIdea = extendedIdea.structuredIdea
            )
            // Update idea remotely
            ideaRemoteDataSource.updateIdea(updatedIdea.toDto())
            // Update idea locally
            ideaDao.insertIdea(updatedIdea.toEntity())
            ideaDao.insertQuestions(updatedIdea.questions.map({ it.toEntity() }))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
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