package com.example.mindflow.domain.model

data class Question(
    val id: String,
    val ideaId: String,
    val category: String,
    val questionText: String,
    val description: String,
    val userAnswer: String? = null
    )
