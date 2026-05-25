package com.example.mindflow.domain.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val password: String,
    val mail: String,
    val isSubscribed: Boolean,
    val ideas: List<Idea>
    )