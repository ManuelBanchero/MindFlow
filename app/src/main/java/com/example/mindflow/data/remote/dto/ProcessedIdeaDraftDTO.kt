package com.example.mindflow.data.remote.dto

import com.example.mindflow.domain.model.Question
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessedIdeaDraftDTO(
    @SerializedName("title")
    val title: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("summarize_content")
    val summarizeContent: String,
    @SerializedName("structured_idea")
    val structuredIdea: String,
    @SerializedName("questions")
    val questions: List<Question>
)