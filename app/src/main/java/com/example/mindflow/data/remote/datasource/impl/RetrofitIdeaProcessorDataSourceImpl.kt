package com.example.mindflow.data.remote.datasource.impl

import android.content.Context
import android.net.Uri
import com.example.mindflow.data.remote.api.MindFlowApiService
import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.StructuredSectionDTO
import com.example.mindflow.data.remote.extensions.audioUriToMultiPart
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RetrofitIdeaProcessorDataSourceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val apiService: MindFlowApiService
): IdeaProcessorDataSource {
    override suspend fun processAudio(audioUri: Uri): ProcessedIdeaDraftDTO {
        val audioPart = context.audioUriToMultiPart(audioUri)

        return apiService.processIdea(audioPart)
    }

    override suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: List<StructuredSectionDTO>,
        audioUri: Uri
    ): ProcessedIdeaDraftDTO {
        TODO("Not yet implemented")
    }

    override suspend fun expandIdeaWithAnswerQuestion(
        ideaTitle: String,
        ideaContent: List<StructuredSectionDTO>,
        question: String,
        questionDescription: String,
        audioUri: Uri
    ): ProcessedAnswerQuestionDTO {
        TODO("Not yet implemented")
    }
}