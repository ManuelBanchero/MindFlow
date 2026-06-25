package com.example.mindflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = IdeaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idea_id"],
            onDelete = ForeignKey.CASCADE // If an idea is deleted, all questions associated too
        )
    ],
    indices = [Index(value = ["idea_id"])] // Create an index makes the deletion faster
)
data class QuestionEntity(
    @PrimaryKey()
    val id: Int,
    @ColumnInfo(name = "idea_id")
    val ideaId: Int,
    val category: String,
    @ColumnInfo(name = "question_text")
    val questionText: String,
    val description: String,
    @ColumnInfo(name = "was_answered")
    val wasAnswered: Boolean = false,
)
