package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO

interface IdeaProcessorDataSource {
    suspend fun processRawText(text: String): ProcessedIdeaDraftDTO
    suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: String,
        newContext: String
    ): ProcessedIdeaDraftDTO
}