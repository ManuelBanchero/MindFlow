package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessedIdeaDraftDTO(
    @SerialName("title")
    val title: String,
    @SerialName("category")
    val category: String,
    @SerialName("summarizeContent")
    val summarizeContent: String,
    @SerialName("structuredIdea")
    val structuredIdea: List<StructuredSectionDTO>,
    @SerialName("questions")
    val questions: List<ProcessedQuestionDraftDTO>,
    @SerialName("transcription")
    val transcription: String
)