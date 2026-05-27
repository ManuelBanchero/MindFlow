package com.example.mindflow.data.remote.datasource

import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO

interface IdeaProcessorDataSource {
    suspend fun processRawText(text: String): ProcessedIdeaDraftDTO
    suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: String,
        newContext: String
    ): ProcessedIdeaDraftDTO

    suspend fun expandIdeaWithAnswerQuestion(
        ideaTitle: String,
        ideaContent: String,
        question: String,
        questionDescription: String,
        answer: String
    ): ProcessedAnswerQuestionDTO
}