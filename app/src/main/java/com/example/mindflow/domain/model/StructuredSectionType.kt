package com.example.mindflow.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class StructuredSectionType {
    MAIN_IDEA,
    CONTEXT,
    OBJECTIVE,
    PROBLEM,
    SOLUTION,
    TARGET_AUDIENCE,
    BENEFITS,
    RISKS,
    IMPLEMENTATION,
    RESOURCES,
    PROCESS,
    INGREDIENTS,
    STEPS,
    CONTENT_STRUCTURE,
    SCRIPT,
    DESIGN,
    MARKETING,
    MONETIZATION,
    NEXT_STEPS,
    CONCLUSION,
    OTHER
}