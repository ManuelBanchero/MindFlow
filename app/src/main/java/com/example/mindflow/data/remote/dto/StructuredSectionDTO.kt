package com.example.mindflow.data.remote.dto

import com.example.mindflow.domain.model.StructuredSectionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StructuredSectionDTO(
    @SerialName("type")
    val type: StructuredSectionType,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String
)