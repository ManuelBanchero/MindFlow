package com.example.mindflow.data.local.file.impl

import android.content.Context
import com.example.mindflow.data.local.file.FileManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class InternalFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) : FileManager {

    override fun createTempFile(extension: String): File {
        val fileName = "temp_recording_${UUID.randomUUID()}.$extension"
        return File(context.cacheDir, fileName)
    }

    override fun saveToInternalStorage(tempFile: File, newName: String): File {
        val destFile = File(context.filesDir, newName)
        
        // Try to rename first, it's more efficient
        if (!tempFile.renameTo(destFile)) {
            // If rename fails (e.g. across different filesystems), copy the content
            FileInputStream(tempFile).use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile.delete()
        }
        return destFile
    }

    override fun deleteFile(path: String): Boolean {
        val file = File(path)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    override fun exists(path: String): Boolean {
        return File(path).exists()
    }
}