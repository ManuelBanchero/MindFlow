package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdeaDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("userId")
    val userId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("createdAt")
    val createdAt: Long,
    @SerialName("updatedAt")
    val updatedAt: Long,
    @SerialName("category")
    val category: String,
    @SerialName("textsAudioHistory")
    val textsAudioHistory: List<String>,
    @SerialName("summarizeContent")
    val summarizeContent: String,
    @SerialName("structuredIdea")
    val structuredIdea: List<StructuredSectionDTO>,
    @SerialName("questions")
    val questions: List<QuestionDTO>
)