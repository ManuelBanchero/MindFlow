package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO (
    @SerialName("id")
    val id: Int = 0,
    @SerialName("ideaId")
    val ideaId: Int = 0,
    @SerialName("category")
    val category: String,
    @SerialName("questionText")
    val questionText: String,
    @SerialName("description")
    val description: String,
    @SerialName("wasAnswered")
    val wasAnswered: Boolean = false
)
