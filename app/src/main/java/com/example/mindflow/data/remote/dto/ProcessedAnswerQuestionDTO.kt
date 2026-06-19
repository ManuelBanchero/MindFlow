package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessedAnswerQuestionDTO(
    @SerialName("summarizeContent")
    val summarizeContent: String,
    @SerialName("structuredIdea")
    val structuredIdea: List<StructuredSectionDTO>,
    @SerialName("transcription")
    val transcription: String
)