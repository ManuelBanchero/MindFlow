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

    override suspend fun processIdea(audioUri: String, userId: Int): Result<IdeaDTO> {
        return try {
            val uri: Uri = audioUri.toUri()

            // Process audio and create an Idea (also saves the idea in the cloud)
            val ideaDto = ideaProcessorDataSource.processIdea(uri, userId)

            // Deletes audio from user storage
            ideaProcessorDataSource.deleteAudio(uri)

            Result.success(ideaDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveIdea(ideaDTO: IdeaDTO, userId: Int): Result<Int> {
        return try {
            persistIdeaLocally(ideaDTO)

            Result.success(ideaDTO.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncUserIdeas(userId: Int): Result<Unit> {
        return try {
            val remoteIdeas = ideaRemoteDataSource.getUserIdeas(userId)

            database.withTransaction {
                ideaDao.deleteAllIdeas()
                remoteIdeas.forEach { ideaDto ->
                    ideaDao.upsertIdea(ideaDto.toEntity())
                    ideaDao.upsertQuestions(
                        ideaDto.questions.mapIndexed { index, questionDto ->
                            questionDto.toEntity(
                                generatedId = if (questionDto.id != 0) questionDto.id else index + 1,
                                ideaIdOverride = ideaDto.id
                            )
                        }
                    )
                }
            }

            Result.success(Unit)
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

    override suspend fun deleteAllLocalIdeas(): Result<Unit> {
        return try {
            database.withTransaction {
                ideaDao.deleteAllIdeas()
            }
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
                idea.structuredIdea.map { it.toDto() },
                uri
            )
            ideaProcessorDataSource.deleteAudio(uri)

            // Convertimos los QuestionDraft sugeridos a Questions reales (id=0 para que Room genere el nuevo)
            val newQuestions = extendedIdeaDto.questions.map { dto ->
                val draft = dto.toDomain()
                Question(
                    id = 0,
                    ideaId = idea.id,
                    category = draft.category,
                    questionText = draft.questionText,
                    description = draft.description,
                    wasAnswered = false
                )
            }

            val updatedIdea = idea.copy(
                title = extendedIdeaDto.title,
                updatedAt = Instant.now(),
                categories = parseCategories(extendedIdeaDto.category),
                textsAudiosHistory = idea.textsAudiosHistory + extendedIdeaDto.transcription,
                summarizeContent = extendedIdeaDto.summarizeContent,
                structuredIdea = extendedIdeaDto.structuredIdea.map { it.toDomain() },
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

            // Validate if the questions exists
            ideaDao.getQuestionById(questionId)
                ?: throw IllegalArgumentException("No se encontró la pregunta con ID $questionId")

            // Expand and save in cloud
            val ideaDto = ideaProcessorDataSource.expandIdeaWithAnswerQuestion(
                idea.id,
                questionId,
                uri
            )

            // Delete audio from user files
            ideaProcessorDataSource.deleteAudio(uri)

            // update locally
            database.withTransaction {
                ideaDao.upsertIdea(ideaDto.toEntity())
                ideaDao.deleteQuestionsByIdeaId(ideaDto.id)
                ideaDao.upsertQuestions(
                    ideaDto.questions.mapIndexed { index, questionDto ->
                        questionDto.toEntity(
                            generatedId = if (questionDto.id != 0) questionDto.id else index + 1,
                            ideaIdOverride = ideaDto.id
                        )
                    }
                )
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

    private fun parseCategories(raw: String): List<String> {
        return raw.split("/", ",", ";", "|")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
    }

    private suspend fun persistIdeaLocally(ideaDTO: IdeaDTO) {
        database.withTransaction {
            ideaDao.upsertIdea(ideaDTO.toEntity())
            ideaDao.upsertQuestions(
                ideaDTO.questions.mapIndexed { index, questionDto ->
                    questionDto.toEntity(
                        generatedId = if (questionDto.id != 0) questionDto.id else index + 1,
                        ideaIdOverride = ideaDTO.id
                    )
                }
            )
        }
    }
}
