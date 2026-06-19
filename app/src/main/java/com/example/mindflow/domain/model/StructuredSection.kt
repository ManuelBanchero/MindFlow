package com.example.mindflow.domain.model

data class StructuredSection(
    val type: StructuredSectionType,
    val title: String,
    val content: String
)