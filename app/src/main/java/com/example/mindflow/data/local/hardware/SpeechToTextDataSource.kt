package com.example.mindflow.data.local.hardware

import android.net.Uri

interface SpeechToTextDataSource {
    suspend fun transcribeAudio(audioUri: String): String
}