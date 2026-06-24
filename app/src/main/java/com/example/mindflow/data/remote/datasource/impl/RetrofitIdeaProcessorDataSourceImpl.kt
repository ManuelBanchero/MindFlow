package com.example.mindflow.data.remote.datasource.impl

import android.content.Context
import android.net.Uri
import com.example.mindflow.data.remote.api.MindFlowApiService
import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.dto.IdeaDTO
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
    override suspend fun processIdea(audioUri: Uri): IdeaDTO {
        val audioPart = context.audioUriToMultiPart(audioUri)

        return apiService.processIdea(audioPart)
    }

    override suspend fun deleteAudio(audioUri: Uri) {
        val filePath = when {
            audioUri.scheme == "file" -> audioUri.path
            audioUri.path?.startsWith("/") == true -> audioUri.path
            else -> null
        } ?: return

        runCatching {
            java.io.File(filePath).takeIf { it.exists() }?.delete()
        }
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
