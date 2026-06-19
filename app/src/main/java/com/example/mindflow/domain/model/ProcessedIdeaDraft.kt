package com.example.mindflow.domain.model

data class ProcessedIdeaDraft(
    val title: String,
    val category: String,
    val summarizeContent: String,
    val structuredIdea: List<StructuredSection>,
    val questions: List<QuestionDraft>,
    val transcription: String
)