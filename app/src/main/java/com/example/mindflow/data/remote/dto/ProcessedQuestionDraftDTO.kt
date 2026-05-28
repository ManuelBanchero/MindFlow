package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessedQuestionDraftDTO(
    @SerialName("category")
    val category: String,
    @SerialName("question_text")
    val questionText: String,
    @SerialName("description")
    val description: String
)
