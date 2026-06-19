package com.example.mindflow.data.remote.extensions

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun Context.audioUriToMultiPart(
    uri: Uri
): MultipartBody.Part {
    val bytes = try {
        val filePath = when {
            uri.scheme == "file" -> uri.path
            uri.path?.startsWith("/") == true -> uri.path
            else -> null
        }

        if (filePath != null) {
            File(filePath).readBytes()
        } else {
            contentResolver.openInputStream(uri)
                ?.use { it.readBytes() }
                ?: throw IllegalArgumentException("Could not open input stream for URI: $uri")
        }
    } catch (e: Exception) {
        throw IllegalArgumentException("Failed to read audio file: ${e.message}", e)
    }

    if (bytes.isEmpty()) {
        throw IllegalArgumentException("Audio file is empty")
    }

    val mimeType = contentResolver.getType(uri) ?: "audio/m4a"

    val requestBody = bytes.toRequestBody(
        mimeType.toMediaType()
    )

    return MultipartBody.Part.createFormData(
        name = "audio",
        filename = "audio.m4a",
        body = requestBody
    )
}