package com.example.mindflow.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mindflow.data.local.entity.IdeaEntity
import com.example.mindflow.data.local.entity.IdeaWithQuestionsRelation
import com.example.mindflow.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao interface IdeaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: IdeaEntity, question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Delete
    suspend fun deleteIdea(idea: IdeaEntity)

    @Transaction
    @Query("SELECT * FROM ideas WHERE user_id = :userId ORDER BY created_at DESC")
    fun getIdeasByUserId(userId: Int): Flow<List<IdeaWithQuestionsRelation>>
}