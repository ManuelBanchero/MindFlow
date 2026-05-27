package com.example.mindflow.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessedAnswerQuestionDTO(
    @SerializedName("summarize_content")
    val summarizeContent: String,
    @SerializedName("structured_idea")
    val structuredIdea: String
)
