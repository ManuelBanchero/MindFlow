package com.example.mindflow.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserIdeasResponse(
    @SerialName("ideas")
    val ideas: List<IdeaDTO>
)
