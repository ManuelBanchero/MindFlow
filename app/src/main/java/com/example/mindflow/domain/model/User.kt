package com.example.mindflow.domain.model

data class User(
    val id: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val mail: String,
    val isSubscribed: Boolean,
    )