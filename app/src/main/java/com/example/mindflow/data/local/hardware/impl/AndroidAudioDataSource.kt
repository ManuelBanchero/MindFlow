package com.example.mindflow.data.local.hardware.impl

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import com.example.mindflow.data.local.hardware.AudioDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * Implementación de [AudioDataSource] utilizando la API nativa de Android [MediaRecorder].
 * Esta clase se encarga de configurar los parámetros del hardware para optimizar
 * la captura de voz para su posterior transcripción.
 */
class AndroidAudioDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioDataSource {

    private companion object {
        const val TAG = "MindFlowAudio"
    }

    private var recorder: MediaRecorder? = null

    /**
     * Crea una instancia de MediaRecorder de forma segura según la versión de Android.
     */
    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    override fun start(outputFile: File) {
        recorder = createRecorder().apply {
            Log.d(TAG, "Starting recording at ${outputFile.absolutePath}")

            setAudioSource(MediaRecorder.AudioSource.MIC)
            // Formato de salida: MPEG_4 (compatible con .m4a)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            // Codificador: AAC (Buena compresión y calidad)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            // 16kHz es suficiente para voz y suele ser más estable para speech-to-text.
            setAudioSamplingRate(16000)
            // Bitrate razonable para voz
            setAudioEncodingBitRate(64000)
            // Mono (No hace falta estéreo para voz)
            setAudioChannels(1)
            // Destino: La ruta absoluta del archivo proporcionado por FileManager
            setOutputFile(outputFile.absolutePath)

            prepare()
            start()
        }
    }

    override fun stop() {
        recorder?.apply {
            try {
                stop()
            } catch (e: Exception) {
                Log.w(TAG, "MediaRecorder stop failed", e)
            }
            reset()
            release()
        }
        recorder = null
    }

    override fun pause() {
        recorder?.pause()
    }

    override fun resume() {
        recorder?.resume()
    }
}
