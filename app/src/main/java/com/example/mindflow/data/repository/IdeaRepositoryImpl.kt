package com.example.mindflow.data.repository

import android.net.Uri
import androidx.room.withTransaction
import com.example.mindflow.data.local.entity.QuestionEntity
import com.example.mindflow.data.local.room.dao.IdeaDAO
import com.example.mindflow.data.local.room.database.AppDatabase
import com.example.mindflow.data.mapper.toDomain
import com.example.mindflow.data.mapper.toDto
import com.example.mindflow.data.mapper.toEntity
import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.datasource.IdeaRemoteDataSource
import com.example.mindflow.data.local.hardware.SpeechToTextDataSource
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.domain.model.ProcessedIdeaResult
import com.example.mindflow.domain.repository.IdeaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import androidx.core.net.toUri

class IdeaRepositoryImpl(
   private val database: AppDatabase,
   private val ideaDao: IdeaDAO,
   private val ideaRemoteDataSource: IdeaRemoteDataSource,
   private val speechToTextDataSource: SpeechToTextDataSource,
   private val ideaProcessorDataSource: IdeaProcessorDataSource
): IdeaRepository {
    override suspend fun processIdea(audioUri: String): Result<ProcessedIdeaResult> {
        return try {
            // Transform uri string to Uri
            val uri: Uri = audioUri.toUri()
            val audioContent: String = speechToTextDataSource.transcribeAudio(uri)
            val processedIdeaDto = ideaProcessorDataSource.processRawText(audioContent)

            Result.success(
                ProcessedIdeaResult(
                    draft = processedIdeaDto.toDomain(),
                    audioTranscribed = audioContent
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIdea(processedIdea: ProcessedIdeaResult, userId: Int): Result<Int> {
        return try {
            val (draft, audioTranscribed) = processedIdea
            // Save it remotely
            val idea: IdeaDTO = ideaRemoteDataSource.saveIdea(
                draft.toDto(),
                audioTranscribed,
                userId
            )
            // Save it locally
            database.withTransaction {
                ideaDao.upsertIdea(idea.toEntity())
                ideaDao.upsertQuestions(
                    idea.questions.map{ it.toEntity()}
                )
            }

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
            database.withTransaction {
                ideaDao.upsertIdea(idea.toEntity())
                ideaDao.upsertQuestions(
                    idea.questions.map{ it.toEntity() }
                )
            }

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
        audioUri: String
    ): Result<Unit> {
        return try {
            // Transform uri string to uri
            val uri: Uri = audioUri.toUri()
            // Get new context
            val newIdeaContext = speechToTextDataSource.transcribeAudio(uri)
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
                structuredIdea = extendedIdea.structuredIdea,
                questions = idea.questions + extendedIdea.questions.map { it.toDomain()}
            )
            // Update idea remotely
            ideaRemoteDataSource.updateIdea(updatedIdea.toDto())
            // Update idea locally
            database.withTransaction {
                ideaDao.upsertIdea(updatedIdea.toEntity())
                ideaDao.upsertQuestions(updatedIdea.questions.map{ it.toEntity() })
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun answerQuestion(
        idea: Idea,
        questionId: Int,
        audioUri: String
    ): Result<Unit> {
        return try {
            // Transform uri string to uri
            val uri: Uri = audioUri.toUri()

            val userAnswer = speechToTextDataSource.transcribeAudio(uri)
            val question: QuestionEntity = ideaDao.getQuestionById(questionId)
                ?: throw IllegalArgumentException("No se encontró la pregunta con ID $questionId")

            val processedIdeaAfterAnswer = ideaProcessorDataSource.expandIdeaWithAnswerQuestion(
                idea.title,
                idea.structuredIdea,
                question.questionText,
                question.description,
                userAnswer
            )

            // Create an updated idea
            val updatedIdea: Idea = idea.copy(
                summarizeContent = processedIdeaAfterAnswer.summarizeContent,
                structuredIdea = processedIdeaAfterAnswer.structuredIdea
            )

            // Update remotely
            ideaRemoteDataSource.updateIdea(updatedIdea.toDto())
            // Update locally
            database.withTransaction {
                ideaDao.upsertIdea(updatedIdea.toEntity())
                ideaDao.deleteQuestionById(questionId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getIdeasFlow(userId: Int): Flow<List<Idea>> {
        return ideaDao.getIdeasByUserId(userId).map{ list ->
            list.map{ it.toDomain() }
        }
    }

    override fun getIdeaById(
        ideaId: Int,
        userId: Int
    ): Flow<Idea?> {
        return ideaDao.getIdeaById(ideaId, userId).map{ it?.toDomain() }
    }
}