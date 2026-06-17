package com.example.mindflow.domain.model

data class ProcessedIdeaDraft(
    val title: String,
    val category: String,
    val summarizeContent: String,
    val structuredIdea: String,
    val questions: List<QuestionDraft>,
    val transcription: String
)