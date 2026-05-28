package com.example.mindflow.data.mapper

import com.example.mindflow.data.local.entity.IdeaEntity
import com.example.mindflow.data.local.entity.IdeaWithQuestionsRelation
import com.example.mindflow.data.local.entity.QuestionEntity
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.ProcessedQuestionDraftDTO
import com.example.mindflow.data.remote.dto.QuestionDTO
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.ProcessedIdeaDraft
import com.example.mindflow.domain.model.Question
import java.time.Instant

// --- Entity -> Domain ---

fun QuestionEntity.toDomain(): Question {
    return Question(
        id = this.id,
        ideaId = this.ideaId,
        category = this.category,
        questionText = this.questionText,
        description = this.description
    )
}

fun IdeaWithQuestionsRelation.toDomain(): Idea {
    return Idea(
        id = this.idea.id,
        userId = this.idea.userId,
        title = this.idea.title,
        // De Long (DB) a Instant (Dominio)
        createdAt = Instant.ofEpochMilli(this.idea.createdAt),
        updatedAt = Instant.ofEpochMilli(this.idea.updatedAt),
        category = this.idea.category,
        textsAudiosHistory = this.idea.textsAudioHistory.split("\n\n"),
        summarizeContent = this.idea.summarizeContent,
        structuredIdea = this.idea.structuredIdea,
        questions = this.questions.map { it.toDomain() }
    )
}

// --- Domain -> Entity ---

fun Idea.toEntity(): IdeaEntity {
    return IdeaEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        // De Instant (Dominio) a Long (DB)
        createdAt = this.createdAt.toEpochMilli(),
        updatedAt = this.updatedAt.toEpochMilli(),
        category = this.category,
        textsAudioHistory = this.textsAudiosHistory.joinToString("\n\n"),
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea
    )
}

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = this.id,
        ideaId = this.ideaId,
        category = this.category,
        questionText = this.questionText,
        description = this.description,
    )
}

// --- DTO -> Entity ---

fun IdeaDTO.toEntity(): IdeaEntity {
    return IdeaEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        // Ambos son Long, paso directo
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        category = this.category,
        textsAudioHistory = this.textsAudioHistory.joinToString("\n\n"),
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea
    )
}

fun QuestionDTO.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = this.id,
        ideaId = this.ideaId,
        category = this.category,
        questionText = this.questionText,
        description = this.description,
    )
}

// --- Domain -> DTO ---

fun Question.toDto(): QuestionDTO {
    return QuestionDTO(
        id = this.id,
        ideaId = this.ideaId,
        category = this.category,
        questionText = this.questionText,
        description = this.description
    )
}

fun Idea.toDto(): IdeaDTO {
    return IdeaDTO(
        id = this.id,
        userId = this.userId,
        title = this.title,
        // Convertimos Instant a Long para el DTO
        createdAt = this.createdAt.toEpochMilli(),
        updatedAt = this.updatedAt.toEpochMilli(),
        category = this.category,
        textsAudioHistory = this.textsAudiosHistory,
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea,
        questions = this.questions.map { it.toDto() }
    )
}

// --- Drafts mappings ---

fun ProcessedQuestionDraftDTO.toDomain(): Question {
    return Question(
        id = 0,
        ideaId = 0,
        category = this.category,
        questionText = this.questionText,
        description = this.description
    )
}

fun ProcessedIdeaDraftDTO.toDomain(): ProcessedIdeaDraft {
    return ProcessedIdeaDraft(
        title = this.title,
        category = this.category,
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea,
        questions = this.questions.map { it.toDomain() }
    )
}

fun Question.toDraftDto(): ProcessedQuestionDraftDTO {
    return ProcessedQuestionDraftDTO(
        category = this.category,
        questionText = this.questionText,
        description = this.description
    )
}

fun ProcessedIdeaDraft.toDto(): ProcessedIdeaDraftDTO {
    return ProcessedIdeaDraftDTO(
        title = this.title,
        category = this.category,
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea,
        questions = this.questions.map { it.toDraftDto() }
    )
}