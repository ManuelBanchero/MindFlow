package com.example.mindflow.data.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "ideas")
data class IdeaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    val category: String,
    @ColumnInfo(name = "texts_audio_history")
    val textsAudioHistory: String, // En vez de una lista, ahora es un String, dividido por \n\n,
    @ColumnInfo(name = "summarize_content")
    val summarizeContent: String,
    @ColumnInfo(name = "structured_idea")
    val structuredIdea: String
)