package com.example.mindflow.data.local.hardware

import java.io.File

/**
 * Interfaz que define las operaciones básicas de captura de audio a nivel de hardware.
 * Esta abstracción permite desacoplar el motor de grabación (MediaRecorder, Oboe, etc.)
 * del resto de la lógica de la aplicación.
 */
interface AudioDataSource {
    /**
     * Inicia la captura de audio y la guarda en el archivo especificado.
     * @param outputFile El archivo donde se escribirá el flujo de audio.
     */
    fun start(outputFile: File)

    /**
     * Detiene la captura de audio actual y libera los recursos del hardware.
     */
    fun stop()

    /**
     * Pausa la grabación actual.
     */
    fun pause()

    /**
     * Reanuda una grabación previamente pausada.
     */
    fun resume()
}