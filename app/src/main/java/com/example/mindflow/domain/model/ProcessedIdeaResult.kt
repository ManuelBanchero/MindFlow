package com.example.mindflow.domain.model

data class ProcessedIdeaResult(
    val draft: ProcessedIdeaDraft, // El modelo de dominio de la idea procesada
    val audioTranscribed: String           // El contenido del audio transcrito
)