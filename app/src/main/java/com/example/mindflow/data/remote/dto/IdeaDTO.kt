package com.example.mindflow.data.remote.dto

import com.example.mindflow.domain.model.Question
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class IdeaDTO(
    val id: Int,
    val userId: Int,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val category: String,
    val textsAudioHistory: List<String>,
    val summarizeContent: String,
    val structuredIdea: String,
    val questions: List<Question>
    )
