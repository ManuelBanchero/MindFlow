package com.example.mindflow.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO (
    @SerializedName("id")
    val id: Int,
    @SerializedName("idea_id")
    val ideaId: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("quetion_text")
    val questionText: String,
    @SerializedName("description")
    val description: String
    )