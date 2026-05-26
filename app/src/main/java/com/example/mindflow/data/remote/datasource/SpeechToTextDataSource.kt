package com.example.mindflow.data.remote.datasource

interface SpeechToTextDataSource {
    suspend fun transcribeAudio(audioFilePath: String): String
}