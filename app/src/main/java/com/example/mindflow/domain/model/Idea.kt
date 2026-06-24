package com.example.mindflow.domain.model

import java.time.Instant

data class Idea (
    val id: Int,
    val userId: Int,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val categories: List<String>,
    val textsAudiosHistory: List<String>,
    val summarizeContent: String,
    val structuredIdea: List<StructuredSection>,
    val questions: List<Question>
)