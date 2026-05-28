package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.datasource.IdeaRemoteDataSource
import com.example.mindflow.data.remote.dto.IdeaDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.QuestionDTO
import java.time.Instant
import javax.inject.Inject

class MockIdeaRemoteDataSource @Inject constructor(): IdeaRemoteDataSource {
    override suspend fun saveIdea(
        processedIdeaDraftDTO: ProcessedIdeaDraftDTO,
        audioTranscribed: String,
        userId: Int
    ): IdeaDTO {
        return IdeaDTO(
            id = 1,
            userId = 1,
            title = processedIdeaDraftDTO.title,
            createdAt = Instant.now().toEpochMilli(),
            updatedAt = Instant.now().toEpochMilli(),
            category = processedIdeaDraftDTO.category,
            textsAudioHistory = listOf(audioTranscribed),
            summarizeContent = processedIdeaDraftDTO.summarizeContent,
            structuredIdea = processedIdeaDraftDTO.structuredIdea,
            questions = processedIdeaDraftDTO.questions.mapIndexed {
                i, it -> QuestionDTO(
                    id = i + 1,
                    ideaId = 1,
                    category = it.category,
                    questionText = it.questionText,
                    description = it.description
                )
            }
        )
    }

    override suspend fun updateIdea(ideaDTO: IdeaDTO) {
        return
    }

    override suspend fun deleteIdea(userId: Int) {
        return
    }
}
