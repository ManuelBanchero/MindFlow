package com.example.mindflow.data.service

import com.example.mindflow.data.local.file.FileManager
import com.example.mindflow.data.local.hardware.AudioDataSource
import com.example.mindflow.domain.service.AudioRecorder
import com.example.mindflow.domain.service.RecordingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

/**
 * Implementación del servicio de dominio [AudioRecorder].
 * Actúa como orquestador entre el manejo de archivos ([FileManager])
 * y el acceso al hardware ([AudioDataSource]).
 */
class AudioRecorderImpl @Inject constructor(
    private val audioDataSource: AudioDataSource,
    private val fileManager: FileManager
) : AudioRecorder {

    private val _recordingState = MutableStateFlow<RecordingState>(RecordingState.Idle)
    override val recordingState: StateFlow<RecordingState> = _recordingState.asStateFlow()

    private var currentTempFile: File? = null

    override suspend fun startRecord(): Result<Unit> {
        return try {
            // 1. Crear el archivo temporal en la caché
            val tempFile = fileManager.createTempFile("m4a")
            currentTempFile = tempFile

            // 2. Iniciar la grabación en el hardware
            audioDataSource.start(tempFile)

            // 3. Actualizar el estado
            _recordingState.value = RecordingState.Recording
            Result.success(Unit)
        } catch (e: Exception) {
            _recordingState.value = RecordingState.Error(e.message ?: "Error al iniciar la grabación")
            Result.failure(e)
        }
    }

    override suspend fun pauseRecord(): Result<Unit> {
        return try {
            audioDataSource.pause()
            _recordingState.value = RecordingState.Paused
            Result.success(Unit)
        } catch (e: Exception) {
            _recordingState.value = RecordingState.Error(e.message ?: "Error al pausar la grabación")
            Result.failure(e)
        }
    }

    override suspend fun resumeRecord(): Result<Unit> {
        return try {
            audioDataSource.resume()
            _recordingState.value = RecordingState.Recording
            Result.success(Unit)
        } catch (e: Exception) {
            _recordingState.value = RecordingState.Error(e.message ?: "Error al reanudar la grabación")
            Result.failure(e)
        }
    }

    override suspend fun stopRecord(): Result<String> {
        return try {
            // 1. Detener el hardware
            audioDataSource.stop()

            // 2. Obtener la ruta del archivo generado
            val path = currentTempFile?.absolutePath 
                ?: throw IllegalStateException("No hay un archivo de grabación activo")

            // 3. Limpiar estado y devolver éxito con la ruta
            _recordingState.value = RecordingState.Idle
            currentTempFile = null

            // LOGS FOR TEST (DELETE IT)
            println("MIND_FLOW_DEBUG: Grabación guardada en $path")
            println("MIND_FLOW_DEBUG: Tamaño del archivo: ${File(path).length()} bytes")

            Result.success(path)
        } catch (e: Exception) {
            _recordingState.value = RecordingState.Error(e.message ?: "Error al detener la grabación")
            Result.failure(e)
        }
    }
}