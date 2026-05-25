package com.example.mindflow.data.mapper

import com.example.mindflow.data.local.entity.IdeaWithQuestionsRelation
import com.example.mindflow.data.local.entity.QuestionEntity
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