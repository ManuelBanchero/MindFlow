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
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.Question
import com.example.mindflow.domain.model.ProcessedIdeaResult
import com.example.mindflow.domain.repository.IdeaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import androidx.core.net.toUri
import javax.inject.Inject

class IdeaRepositoryImpl @Inject constructor(
   private val database: AppDatabase,
   private val ideaDao: IdeaDAO,
   private val ideaRemoteDataSource: IdeaRemoteDataSource,
   private val ideaProcessorDataSource: IdeaProcessorDataSource
): IdeaRepository {

    override suspend fun processIdea(audioUri: String): Result<ProcessedIdeaResult> {
        return try {
            val uri: Uri = audioUri.toUri()
            val processedIdeaDto = ideaProcessorDataSource.processAudio(uri)

            Result.success(
                ProcessedIdeaResult(
                    draft = processedIdeaDto.toDomain(),
                    audioTranscribed = processedIdeaDto.transcription
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIdea(processedIdea: ProcessedIdeaResult, userId: Int): Result<Int> {
        return try {
            val (draft, audioTranscribed) = processedIdea
            
            val ideaDto: IdeaDTO = ideaRemoteDataSource.saveIdea(
                draft.toDto(),
                audioTranscribed,
                userId
            )
            
            database.withTransaction {
                ideaDao.upsertIdea(ideaDto.toEntity())
                ideaDao.upsertQuestions(
                    ideaDto.questions.map { it.toEntity() }
                )
            }

            Result.success(ideaDto.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateIdea(idea: Idea): Result<Unit> {
        return try {
            ideaRemoteDataSource.updateIdea(idea.toDto())
            database.withTransaction {
                ideaDao.upsertIdea(idea.toEntity())
                ideaDao.upsertQuestions(
                    idea.questions.map { it.toEntity() }
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteIdea(idea: Idea): Result<Unit> {
        return try {
            ideaRemoteDataSource.deleteIdea(idea.id)
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
            val uri: Uri = audioUri.toUri()
            
            val extendedIdeaDto = ideaProcessorDataSource.expandIdeaWithNewContext(
                idea.title,
                idea.structuredIdea,
                uri
            )

            // Convertimos los QuestionDraft sugeridos a Questions reales (id=0 para que Room genere el nuevo)
            val newQuestions = extendedIdeaDto.questions.map { dto ->
                val draft = dto.toDomain()
                Question(
                    id = 0,
                    ideaId = idea.id,
                    category = draft.category,
                    questionText = draft.questionText,
                    description = draft.description
                )
            }

            val updatedIdea = idea.copy(
                title = extendedIdeaDto.title,
                updatedAt = Instant.now(),
                category = extendedIdeaDto.category,
                textsAudiosHistory = idea.textsAudiosHistory + extendedIdeaDto.transcription,
                summarizeContent = extendedIdeaDto.summarizeContent,
                structuredIdea = extendedIdeaDto.structuredIdea,
                questions = idea.questions + newQuestions
            )

            ideaRemoteDataSource.updateIdea(updatedIdea.toDto())
            database.withTransaction {
                ideaDao.upsertIdea(updatedIdea.toEntity())
                ideaDao.upsertQuestions(updatedIdea.questions.map { it.toEntity() })
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
            val uri: Uri = audioUri.toUri()

            val questionEntity = ideaDao.getQuestionById(questionId)
                ?: throw IllegalArgumentException("No se encontró la pregunta con ID $questionId")

            val processedAnswerDto = ideaProcessorDataSource.expandIdeaWithAnswerQuestion(
                idea.title,
                idea.structuredIdea,
                questionEntity.questionText,
                questionEntity.description,
                uri
            )

            // Creamos la idea actualizada eliminando la pregunta respondida de la lista
            val updatedIdea = idea.copy(
                summarizeContent = processedAnswerDto.summarizeContent,
                structuredIdea = processedAnswerDto.structuredIdea,
                textsAudiosHistory = idea.textsAudiosHistory + processedAnswerDto.transcription,
                questions = idea.questions.filter { it.id != questionId }
            )

            ideaRemoteDataSource.updateIdea(updatedIdea.toDto())
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
        return ideaDao.getIdeasByUserId(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getIdeaById(ideaId: Int, userId: Int): Flow<Idea?> {
        return ideaDao.getIdeaById(ideaId, userId).map { it?.toDomain() }
    }
}