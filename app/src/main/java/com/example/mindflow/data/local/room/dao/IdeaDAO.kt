package com.example.mindflow.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.mindflow.data.local.entity.IdeaEntity
import com.example.mindflow.data.local.entity.IdeaWithQuestionsRelation
import com.example.mindflow.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDAO {
    @Upsert
    suspend fun upsertIdea(idea: IdeaEntity): Long

    @Upsert
    suspend fun upsertQuestions(questions: List<QuestionEntity>)

    @Delete
    suspend fun deleteIdea(idea: IdeaEntity)

    @Transaction
    @Query("SELECT * FROM ideas WHERE user_id = :userId ORDER BY created_at DESC")
    fun getIdeasByUserId(userId: Int): Flow<List<IdeaWithQuestionsRelation>>

    @Transaction
    @Query("SELECT * FROM ideas WHERE user_id = :userId AND id = :ideaId")
    fun getIdeaById(ideaId: Int, userId: Int): Flow<IdeaWithQuestionsRelation?>
}