package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessedAnswerQuestionDTO(
    @SerialName("summarize_content")
    val summarizeContent: String,
    @SerialName("structured_idea")
    val structuredIdea: List<StructuredSectionDTO>,
    @SerialName("transcription")
    val transcription: String
)