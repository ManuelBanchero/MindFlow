package com.example.mindflow.data.mapper

import androidx.compose.ui.util.fastJoinToString
import com.example.mindflow.data.local.entity.IdeaEntity
import com.example.mindflow.data.local.entity.IdeaWithQuestionsRelation
import com.example.mindflow.data.local.entity.QuestionEntity
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.domain.model.Idea
import com.example.mindflow.domain.model.Question
import java.time.Instant

fun QuestionEntity.toDomain(): Question {
    return Question(
        id = this.id,
        ideaId = this.ideaId,
        category = this.category,
        questionText = this.questionText,
        description = this.description,
        userAnswer = this.userAnswer
    )
}

fun IdeaWithQuestionsRelation.toDomain(): Idea {
    return Idea(
        id = this.idea.id,
        userId = this.idea.userId,
        title = this.idea.title,
        createdAt = Instant.ofEpochMilli(this.idea.createdAt),
        updatedAt = Instant.ofEpochMilli(this.idea.updatedAt),
        category = this.idea.category,
        textsAudiosHistory = this.idea.textsAudioHistory.split("\n\n"),
        summarizeContent = this.idea.summarizeContent,
        structuredIdea = this.idea.structuredIdea,
        questions = this.questions.map { it.toDomain() }
    )
}

fun Idea.toEntity(): IdeaEntity {
    return IdeaEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
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
        userAnswer = this.userAnswer
    )
}

fun IdeaDTO.toEntity(): IdeaEntity {
    return IdeaEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        createdAt = this.createdAt.toEpochMilli(),
        updatedAt = this.updatedAt.toEpochMilli(),
        category = this.category,
        textsAudioHistory = this.textsAudioHistory.joinToString("\n\n"),
        summarizeContent = this.summarizeContent,
        structuredIdea = this.structuredIdea
    )
}