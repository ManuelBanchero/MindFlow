package com.example.mindflow.data.remote.datasource

import android.net.Uri
import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO

interface IdeaProcessorDataSource {
    suspend fun processAudio(audioUri: Uri): ProcessedIdeaDraftDTO
    suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: String,
        audioUri: Uri
    ): ProcessedIdeaDraftDTO

    suspend fun expandIdeaWithAnswerQuestion(
        ideaTitle: String,
        ideaContent: String,
        question: String,
        questionDescription: String,
        audioUri: Uri
    ): ProcessedAnswerQuestionDTO
}