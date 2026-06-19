package com.example.mindflow.data.local.file

import java.io.File

interface FileManager {
    // Create an empty temp file
    fun createTempFile(extension: String): File

    // Move from cache to storage
    fun saveToInternalStorage(tempFile: File, newName: String): File

    fun deleteFile(path: String): Boolean

    fun exists(path: String): Boolean
}