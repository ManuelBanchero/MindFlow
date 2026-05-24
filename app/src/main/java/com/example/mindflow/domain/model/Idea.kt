package com.example.mindflow.domain.model

import java.time.Instant

data class Idea(
    val id: String,
    val userId: String,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val category: String,
    val textsAudiosHistory: List<String>,
    val summarizeContent: String,
    val structuredIdea: String,
)