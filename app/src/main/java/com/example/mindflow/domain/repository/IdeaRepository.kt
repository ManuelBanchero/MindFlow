package com.example.mindflow.domain.repository

import com.example.mindflow.domain.model.Idea

interface IdeaRepository {
    suspend fun processIdea(ideaContent: String): Result<Idea>

    suspend fun saveIdea(idea: Idea): Result<Unit>
}