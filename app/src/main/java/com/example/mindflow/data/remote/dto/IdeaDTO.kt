package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdeaDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
    @SerialName("category")
    val category: String,
    @SerialName("texts_audio_history")
    val textsAudioHistory: List<String>,
    @SerialName("summarize_content")
    val summarizeContent: String,
    @SerialName("structured_idea")
    val structuredIdea: List<StructuredSectionDTO>,
    @SerialName("questions")
    val questions: List<QuestionDTO>
)