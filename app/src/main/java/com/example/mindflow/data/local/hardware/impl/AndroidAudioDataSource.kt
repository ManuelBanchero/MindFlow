package com.example.mindflow.data.local.hardware.impl

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
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
            // Fuente: Micrófono
            setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            // Formato de salida: MPEG_4 (compatible con .m4a)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            // Codificador: AAC (Buena compresión y calidad)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            // 16kHz: Ideal para Speech-to-Text
            setAudioSamplingRate(44100)
            // Bitrate razonable para voz
            setAudioEncodingBitRate(128000)
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
                // Manejar posible error si se llama stop() demasiado rápido
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