package com.example.mindflow.data.local.hardware

interface SpeechToTextDataSource {
    suspend fun transcribeAudio(audioFilePath: String): String
}