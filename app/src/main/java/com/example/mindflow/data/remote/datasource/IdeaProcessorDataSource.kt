package com.example.mindflow.data.remote.datasource

import com.example.mindflow.domain.model.ProcessedIdeaDraft

interface IdeaProcessorDataSource {
    suspend fun processRawText(text: String): ProcessedIdeaDraft
}