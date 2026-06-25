package com.example.mindflow.data.remote.datasource

import android.net.Uri
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.StructuredSectionDTO

interface IdeaProcessorDataSource {
    suspend fun processIdea(audioUri: Uri, userId: Int): IdeaDTO
    suspend fun deleteAudio(audioUri: Uri)

    suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: List<StructuredSectionDTO>,
        audioUri: Uri
    ): ProcessedIdeaDraftDTO

    suspend fun expandIdeaWithAnswerQuestion(
        ideaId: Int,
        questionId: Int,
        audioUri: Uri
    ): IdeaDTO
}
