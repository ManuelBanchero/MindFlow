package com.example.mindflow.data.remote.dto

data class UserDTO(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val mail: String,
    val isSubscribed: Boolean
)
