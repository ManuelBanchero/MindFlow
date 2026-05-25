package com.example.mindflow.data.local.entity

import androidx.room3.Embedded
import androidx.room3.Relation

data class IdeaWithQuestionsRelation(
    @Embedded
    val idea: IdeaEntity,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["idea_id"]
    )
    val questions: List<QuestionEntity>
)