package com.example.mindflow.data.mapper

import com.example.mindflow.data.local.entity.UserEntity
import com.example.mindflow.data.local.entity.UserWithIdeasRelation
import com.example.mindflow.domain.model.User

fun UserWithIdeasRelation.toDomain(): User {
    return User(
        id = this.user.id,
        firstName = this.user.firstName,
        lastName = this.user.lastName,
        mail = this.user.mail,
        isSubscribed = this.user.isSubscribed,
        ideas = this.ideas.map { it.toDomain() }
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        mail = this.mail,
        isSubscribed = this.isSubscribed
    )
}