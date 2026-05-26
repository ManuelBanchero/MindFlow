package com.example.mindflow.data.remote.dto

data class LoginRequest(
    val firstName: String,
    val lastName: String,
    val mail: String,
    val password: String
)
