package com.example.mindflow.data.mapper

import com.example.mindflow.data.local.entity.UserEntity
import com.example.mindflow.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        mail = this.mail,
        isSubscribed = this.isSubscribed
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