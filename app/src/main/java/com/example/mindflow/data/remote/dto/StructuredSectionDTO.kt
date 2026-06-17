package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StructuredSectionDTO(
    @SerialName("type")
    val type: String,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String
)