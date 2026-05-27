package com.example.mindflow.domain.usecase

import android.net.Uri
import com.example.mindflow.domain.model.ProcessedIdeaResult
import com.example.mindflow.domain.repository.IdeaRepository

class ProcessIdeaUseCase(
    private val ideaRepository: IdeaRepository
) {
    suspend operator fun invoke(audioUri: Uri): Result<ProcessedIdeaResult> {

    }
}