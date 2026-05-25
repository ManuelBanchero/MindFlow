package com.example.mindflow.data.mapper

import com.example.mindflow.data.local.entity.UserWithIdeasRelation
import com.example.mindflow.domain.model.User

fun UserWithIdeasRelation.toDomain(): User {
    return User(
        id = this.user.id,
        firstName = this.user.firstName,
        lastName = this.user.lastName,
        password = this.user.password,
        mail = this.user.mail,
        isSubscribed = this.user.isSubscribed,
        ideas = this.ideas.map { it.toDomain() }
    )
}