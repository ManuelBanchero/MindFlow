package com.example.mindflow.domain.model

data class User(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val password: String,
    val mail: String,
    val isSubscribed: Boolean,
    )