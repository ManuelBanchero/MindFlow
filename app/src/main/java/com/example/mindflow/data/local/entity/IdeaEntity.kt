package com.example.mindflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mindflow.data.remote.dto.StructuredSectionDTO

@Entity(tableName = "ideas")
data class IdeaEntity(
    @PrimaryKey()
    val id: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    val category: String,
    @ColumnInfo(name = "texts_audio_history")
    val textsAudioHistory: List<String>,
    @ColumnInfo(name = "summarize_content")
    val summarizeContent: String,
    @ColumnInfo(name = "structured_idea")
    val structuredIdea: List<StructuredSectionDTO>
)