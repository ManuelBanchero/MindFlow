package com.example.mindflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = IdeaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idea_id"],
            onDelete = ForeignKey.CASCADE // Si se borra la idea se borran las preguntas
        )
    ]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "idea_id")
    val ideaId: Int,
    val category: String,
    @ColumnInfo(name = "question_text")
    val questionText: String,
    val description: String,
    @ColumnInfo(name = "user_answer")
    val userAnswer: String? = null
)