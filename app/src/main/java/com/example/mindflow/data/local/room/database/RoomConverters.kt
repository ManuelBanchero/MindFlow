package com.example.mindflow.data.local.room.database

import androidx.room.TypeConverter
import com.example.mindflow.data.remote.dto.StructuredSectionDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStructuredSectionList(value: List<StructuredSectionDTO>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toStructuredSectionList(value: String): List<StructuredSectionDTO> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString("\n\n")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split("\n\n").filter { it.isNotBlank() }
    }
}