package com.example.mindflow.data.remote.datasource.impl

import android.net.Uri
import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.ProcessedQuestionDraftDTO
import com.example.mindflow.data.remote.dto.StructuredSectionDTO
import com.example.mindflow.domain.model.StructuredSectionType
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockIdeaProcessorDataSourceImpl @Inject constructor() : IdeaProcessorDataSource {

    override suspend fun processAudio(audioUri: Uri): ProcessedIdeaDraftDTO {
        // Simulating an API response
        delay(2000)
        return ProcessedIdeaDraftDTO(
            title = "MindFlow: Captura Inteligente de Ideas con Arquitectura Limpia",
            category = "Desarrollo Mobile / Android",
            summarizeContent = "Una aplicación Android diseñada para capturar pensamientos desordenados o audios en crudo y estructurarlos automáticamente mediante Inteligencia Artificial.",
            structuredIdea = listOf(
                StructuredSectionDTO(
                    type = StructuredSectionType.MAIN_IDEA,
                    title = "Pilares del Proyecto",
                    content = "1. **Captura Fricción-Cero:** Permite al usuario grabar audios o escribir textos masivos sin preocuparse por el formato.\n2. **Procesamiento con IA:** Uso de modelos de lenguaje para extraer títulos, resúmenes, categorías y formatear el núcleo de la idea.\n3. **Enfoque Pedagógico:** Generación automática de preguntas clave para forzar al usuario a iterar."
                ),
                StructuredSectionDTO(
                    type = StructuredSectionType.IMPLEMENTATION,
                    title = "Arquitectura Técnica",
                    content = "* **Capa de Dominio:** Contiene los modelos puros y las reglas de negocio.\n* **Capa de Data:** Room para persistencia local y Retrofit para red.\n* **Capa de UI:** Implementada con Jetpack Compose."
                )
            ),
            questions = listOf(
                ProcessedQuestionDraftDTO(
                    category = "Arquitectura",
                    questionText = "¿Cómo estructurarás el módulo de Hilt?",
                    description = "Pensar en el uso de @Binds o @Provides para desacoplar la interfaz de su implementación."
                ),
                ProcessedQuestionDraftDTO(
                    category = "Estrategia de Sincronización",
                    questionText = "Si el usuario está offline, ¿cómo encolarás las ideas?",
                    description = "Evaluar el uso de WorkManager de Jetpack."
                )
            ),
            transcription = "Esta es una transcripción de prueba generada por el Mock para el audio de MindFlow sobre arquitectura limpia y procesamiento con IA."
        )
    }

    override suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: List<StructuredSectionDTO>,
        audioUri: Uri
    ): ProcessedIdeaDraftDTO {
        // Simulating an API response
        delay(2000)

        return ProcessedIdeaDraftDTO(
            title = ideaTitle,
            category = "Desarrollo Mobile / Android",
            summarizeContent = "Ampliación de la idea original con nuevo contexto sobre procesamiento local y Whisper.",
            structuredIdea = ideaContent + listOf(
                StructuredSectionDTO(
                    type = StructuredSectionType.OBJECTIVE,
                    title = "Extensión del Alcance",
                    content = "* **Ciclos de Iteración:** El usuario puede responder preguntas de la IA.\n* **Speech-to-Text Local:** Integrar Whisper.tflite para optimización SoC."
                ),
                StructuredSectionDTO(
                    type = StructuredSectionType.NEXT_STEPS,
                    title = "Nuevos pensamientos",
                    content = "Se propone integrar un modelo Whisper optimizado que corra directamente en el hardware del teléfono."
                )
            ),
            questions = listOf(
                ProcessedQuestionDraftDTO(
                    category = "Rendimiento",
                    questionText = "¿Cómo evitarás que el modelo congele la UI?",
                    description = "Asegurar Dispatchers.IO en el DataSource."
                )
            ),
            transcription = "He pensado que podríamos añadir procesamiento local mediante Whisper para mejorar la privacidad y reducir la latencia de red."
        )
    }

    override suspend fun expandIdeaWithAnswerQuestion(
        ideaTitle: String,
        ideaContent: List<StructuredSectionDTO>,
        question: String,
        questionDescription: String,
        audioUri: Uri
    ): ProcessedAnswerQuestionDTO {
        // Simulating an API response
        delay(2000)

        return ProcessedAnswerQuestionDTO(
            summarizeContent = "Consolidación tras responder a la pregunta sobre sincronización y tolerancia a fallos.",
            structuredIdea = ideaContent + listOf(
                StructuredSectionDTO(
                    type = StructuredSectionType.PROCESS,
                    title = "Estrategia de Sincronización",
                    content = "Se incorpora un sistema de reintentos y almacenamiento temporal en Room bajo el estado 'PENDING_STRUCTURE'. Esto garantiza que el usuario nunca pierda información."
                ),
                StructuredSectionDTO(
                    type = StructuredSectionType.BENEFITS,
                    title = "Evolución tras respuesta",
                    content = "La conversión de voz a texto se desacopla de la estructuración semántica."
                )
            ),
            transcription = "Para la sincronización voy a usar WorkManager para asegurar que las ideas se suban incluso si no hay internet en el momento, guardando un estado pendiente en Room."
        )
    }
}
