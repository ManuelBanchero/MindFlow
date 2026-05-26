package com.example.mindflow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val mail: String,
    val isSubscribed: Boolean
)
