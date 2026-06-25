package com.example.mindflow.domain.service

import kotlinx.coroutines.flow.Flow

sealed interface RecordingState {
    data object Idle : RecordingState
    data object Recording : RecordingState
    data object Paused : RecordingState
    data class Error(val message: String) : RecordingState
}

interface AudioRecorder {
    val recordingState: Flow<RecordingState>

    suspend fun startRecord(): Result<Unit>

    suspend fun pauseRecord(): Result<Unit>
    
    suspend fun resumeRecord(): Result<Unit>

    suspend fun stopRecord(): Result<String>

    suspend fun cancelRecord(): Result<Unit>

    fun cleanup()
}
