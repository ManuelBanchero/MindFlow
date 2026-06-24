package com.example.mindflow.data.mapper

import com.example.mindflow.data.local.entity.IdeaEntity
import com.example.mindflow.data.local.entity.IdeaWithQuestionsRelation
import com.example.mindflow.data.local.entity.QuestionEntity
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.ProcessedQuestionDraftDTO
import com.example.mindflow.data.remote.dto.QuestionDTO
import com.example.mindflow.data.remote.dto.StructuredSectionDTO
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.ProcessedIdeaDraft
import com.example.mindflow.domain.model.Question
import com.example.mindflow.domain.model.QuestionDraft
import com.example.mindflow.domain.model.StructuredSection
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
        createdAt = Instant.ofEpochMilli(this.idea.createdAt),
        updatedAt = Instant.ofEpochMilli(this.idea.updatedAt),
        categories = this.idea.categories,
        textsAudiosHistory = this.idea.textsAudioHistory,
        summarizeContent = this.idea.summarizeContent,
        structuredIdea = this.idea.structuredIdea.map { it.toDomain() },
        questions = this.questions.map { it.toDomain() }
    )
}

// --- Domain -> Entity ---

fun Idea.toEntity(): IdeaEntity {
    return IdeaEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        createdAt = this.createdAt.toEpochMilli(),
        updatedAt = this.updatedAt.toEpochMilli(),
        categories = this.categories,
        textsAudioHistory = this.textsAudiosHistory,
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea.map { it.toDto() }
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
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        categories = this.categories,
        textsAudioHistory = this.textsAudioHistory,
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea
    )
}

fun QuestionDTO.toEntity(
    generatedId: Int = this.id,
    ideaIdOverride: Int = this.ideaId
): QuestionEntity {
    return QuestionEntity(
        id = generatedId,
        ideaId = ideaIdOverride,
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
        createdAt = this.createdAt.toEpochMilli(),
        updatedAt = this.updatedAt.toEpochMilli(),
        categories = this.categories,
        textsAudioHistory = this.textsAudiosHistory,
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea.map { it.toDto() },
        questions = this.questions.map { it.toDto() }
    )
}

// --- Section Mappings ---

fun StructuredSectionDTO.toDomain(): StructuredSection {
    return StructuredSection(
        type = this.type,
        title = this.title,
        content = this.content
    )
}

fun StructuredSection.toDto(): StructuredSectionDTO {
    return StructuredSectionDTO(
        type = this.type,
        title = this.title,
        content = this.content
    )
}

// --- Drafts mappings ---

fun ProcessedQuestionDraftDTO.toDomain(): QuestionDraft {
    return QuestionDraft(
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
        structuredIdea = this.structuredIdea.map { it.toDomain() },
        questions = this.questions.map { it.toDomain() },
        transcription = this.transcription
    )
}

fun QuestionDraft.toDraftDto(): ProcessedQuestionDraftDTO {
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
        structuredIdea = this.structuredIdea.map { it.toDto() },
        questions = this.questions.map { it.toDraftDto() },
        transcription = this.transcription
    )
}
