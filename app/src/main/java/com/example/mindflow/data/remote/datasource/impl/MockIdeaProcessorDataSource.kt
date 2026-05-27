package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.QuestionDTO

val ideas: List<ProcessedIdeaDraftDTO> = listOf(
    ProcessedIdeaDraftDTO(

    )
)

class MockIdeaProcessorDataSource(): IdeaProcessorDataSource{
    override suspend fun processRawText(text: String): ProcessedIdeaDraftDTO {
        TODO("Not yet implemented")
    }

    override suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: String,
        newContext: String
    ): ProcessedIdeaDraftDTO {
        TODO("Not yet implemented")
    }

    override suspend fun expandIdeaWithAnswerQuestion(
        ideaTitle: String,
        ideaContent: String,
        question: String,
        questionDescription: String,
        answer: String
    ): ProcessedAnswerQuestionDTO {
        TODO("Not yet implemented")
    }
}