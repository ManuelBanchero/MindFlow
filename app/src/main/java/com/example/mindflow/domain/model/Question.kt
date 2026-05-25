package com.example.mindflow.domain.model

data class Question(
    val id: Int,
    val ideaId: Int,
    val category: String,
    val questionText: String,
    val description: String,
    val userAnswer: String? = null
    )
