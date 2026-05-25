package com.example.mindflow.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithIdeasRelation(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val ideas: List<IdeaWithQuestionsRelation>
)
