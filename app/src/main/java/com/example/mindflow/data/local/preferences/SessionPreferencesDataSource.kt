package com.example.mindflow.data.local.preferences

interface SessionPreferencesDataSource {
    suspend fun isSessionActive(): Boolean
    suspend fun setSessionActive(isActive: Boolean)
}
