package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.ProcessedQuestionDraftDTO
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockIdeaProcessorDataSourceImpl @Inject constructor() : IdeaProcessorDataSource {

    override suspend fun processRawText(text: String): ProcessedIdeaDraftDTO {
        // Simulating an API response
        delay(2000)
        return ProcessedIdeaDraftDTO(
            title = "MindFlow: Captura Inteligente de Ideas con Arquitectura Limpia",
            category = "Desarrollo Mobile / Android",
            summarizeContent = "Una aplicación Android diseñada para capturar pensamientos desordenados o audios en crudo y estructurarlos automáticamente mediante Inteligencia Artificial, utilizando persistencia local con Room y sincronización remota con un backend en Express.",
            structuredIdea = """
        ## Pilares del Proyecto
        1. **Captura Fricción-Cero:** Permite al usuario grabar audios o escribir textos masivos sin preocuparse por el formato.
        2. **Procesamiento con IA:** Uso de modelos de lenguaje para extraer títulos, resúmenes, categorías y formatear el núcleo de la idea en Markdown.
        3. **Enfoque Pedagógico:** Generación automática de preguntas clave para forzar al usuario a iterar y profundizar en sus propios pensamientos.

        ## Arquitectura Técnica (Clean Architecture)
        * **Capa de Dominio:** Contiene los modelos puros (como `ProcessedIdeaDraft`) y las reglas de negocio, totalmente aislada de librerías externas.
        * **Capa de Data (Persistencia y Red):** * **Room:** Almacenamiento local para garantizar el funcionamiento sin conexión.
            * **Retrofit:** Conexión con el servidor Express para la gestión de usuarios.
            * **IdeaProcessorDataSource:** Abstracción para el procesamiento con IA.
        * **Capa de UI:** Implementada con Jetpack Compose, buscando transiciones fluidas mediante estados de carga reactivos.
    """.trimIndent(),
            questions = listOf(
                ProcessedQuestionDraftDTO(
                    category = "Arquitectura",
                    questionText = "¿Cómo estructurarás el módulo de Hilt para alternar fácilmente entre el DataSource de OpenAI y tu clase de Mock?",
                    description = "Pensar en el uso de @Binds o @Provides dentro de un @Module para desacoplar la interfaz de su implementación de pruebas."
                ),
                ProcessedQuestionDraftDTO(
                    category = "Estrategia de Sincronización",
                    questionText = "Si el usuario está offline, ¿cómo encolarás las ideas creadas localmente para que se suban a Express cuando vuelva la señal?",
                    description = "Evaluar el uso de WorkManager de Jetpack para tareas en segundo plano garantizadas y persistentes."
                ),
                ProcessedQuestionDraftDTO(
                    category = "Interfaz de Usuario",
                    questionText = "¿Cómo manejarás el estado mutante de Compose mientras el usuario edita el borrador antes de guardarlo?",
                    description = "Diseñar el estado de la UI en el ViewModel para que la modificación del título o categorías sea temporal hasta la confirmación."
                )
            )
        )
    }

    override suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: String,
        newContext: String
    ): ProcessedIdeaDraftDTO {
        // Simulating an API response
        delay(2000)

        return ProcessedIdeaDraftDTO(
            title = "MindFlow: Captura Inteligente de Ideas con Arquitectura Limpia",
            category = "Desarrollo Mobile / Android",
            summarizeContent = "Una aplicación Android diseñada para capturar pensamientos desordenados o audios en crudo y estructurarlos automáticamente mediante Inteligencia Artificial, utilizando persistencia local con Room y sincronización remota con un backend en Express.",
            structuredIdea = """
        ## Pilares del Proyecto
        1. **Captura Fricción-Cero:** Permite al usuario grabar audios o escribir textos masivos sin preocuparse por el formato.
        2. **Procesamiento con IA:** Uso de modelos de lenguaje para extraer títulos, resúmenes, categorías y formatear el núcleo de la idea en Markdown.
        3. **Enfoque Pedagógico Activo:** Generación automática de preguntas clave para forzar al usuario a iterar y profundizar en sus propios pensamientos.

        ## Extensión del Alcance (Nuevos pensamientos por voz)
        * **Ciclos de Iteración de Ideas:** El usuario ahora puede seleccionar una pregunta generada por la IA, responderla directamente en la interfaz, y solicitar un re-procesamiento. Esto permite que el borrador evolucione dinámicamente antes de su guardado definitivo.
        * **Speech-to-Text Local (Optimización SoC):** Para mitigar problemas de latencia de red en la presentación del jueves, se propone integrar un modelo Whisper optimizado mediante TensorFlow Lite (`Whisper.tflite`) que corra directamente en el hardware del teléfono (NPU/GPU).

        ## Arquitectura Técnica (Clean Architecture)
        * **Capa de Dominio:** Contiene los modelos puros (como `ProcessedIdeaDraft`) y las reglas de negocio, totalmente aislada de librerías externas.
        * **Capa de Data (Persistencia y Red):**
            * **Room:** Almacenamiento local para garantizar el funcionamiento sin conexión.
            * **Retrofit:** Conexión con el servidor Express para la gestión de usuarios.
            * **SpeechToTextDataSource:** Nuevo componente local para gestionar el procesamiento de audio en el chip del dispositivo mediante TFLite.
            * **IdeaProcessorDataSource:** Abstracción para el procesamiento y estructuración del texto con IA.
        * **Capa de UI:** Implementada con Jetpack Compose, buscando transiciones fluidas mediante estados de carga reactivos.
    """.trimIndent(),
            questions = listOf(
                ProcessedQuestionDraftDTO(
                    category = "Arquitectura",
                    questionText = "¿Cómo estructurarás el módulo de Hilt para alternar fácilmente entre el DataSource de OpenAI y tu clase de Mock?",
                    description = "Pensar en el uso de @Binds o @Provides dentro de un @Module para desacoplar la interfaz de su implementación de pruebas."
                ),
                ProcessedQuestionDraftDTO(
                    category = "Estrategia de Sincronización",
                    questionText = "Si el usuario está offline, ¿cómo encolarás las ideas creadas localmente para que se suban a Express cuando vuelva la señal?",
                    description = "Evaluar el uso de WorkManager de Jetpack para tareas en segundo plano garantizadas y persistentes."
                ),
                ProcessedQuestionDraftDTO(
                    category = "Rendimiento Local",
                    questionText = "¿Cómo evitarás que la inicialización del modelo Whisper.tflite congele la interfaz de usuario al abrir la pantalla de grabación?",
                    description = "Asegurar que la carga de los pesos del modelo de IA se ejecute en un hilo secundario utilizando Dispatchers.IO dentro del DataSource."
                )
            )
        )
    }

    override suspend fun expandIdeaWithAnswerQuestion(
        ideaTitle: String,
        ideaContent: String,
        question: String,
        questionDescription: String,
        answer: String
    ): ProcessedAnswerQuestionDTO {
        // Simulating an API response
        delay(2000)

        return ProcessedAnswerQuestionDTO(
            summarizeContent = "Consolidación del flujo técnico de MindFlow mediante un sistema de procesamiento en dos etapas. La captura de voz se resuelve de forma local y offline utilizando Whisper.tflite en el SoC del dispositivo, mientras que la estructuración semántica avanzada se delega a la API en la nube. Se incorpora una estrategia de tolerancia a fallos en el Repositorio que almacena el texto crudo en Room bajo el estado 'PENDING_STRUCTURE' si el teléfono se queda sin conectividad, asegurando que el usuario nunca pierda la información volcada.",
            structuredIdea = "## Pilares del Proyecto\n" +
                    "1. **Captura Fricción-Cero:** Permite al usuario grabar audios o escribir textos masivos sin preocuparse por el formato.\n" +
                    "2. **Procesamiento con IA:** Uso de modelos de lenguaje para extraer títulos, resúmenes, categorías y formatear el núcleo de la idea en Markdown.\n" +
                    "3. **Enfoque Pedagógico Activo:** Generación automática de preguntas clave para forzar al usuario a iterar y profundizar en sus propios pensamientos.\n" +
                    "\n" +
                    "## Extensión del Alcance (Nuevos pensamientos por voz)\n" +
                    "* **Ciclos de Iteración de Ideas:** El usuario ahora puede seleccionar una pregunta generada por la IA, responderla directamente en la interfaz, y solicitar un re-procesamiento. Esto permite que el borrador evolucione dinámicamente antes de su guardado definitivo.\n" +
                    "* **Speech-to-Text Local (Optimización SoC):** Para mitigar problemas de latencia de red en la presentación del jueves, se propone integrar un modelo Whisper optimizado mediante TensorFlow Lite (`Whisper.tflite`) que corra directamente en el hardware del teléfono (NPU/GPU).\n" +
                    "\n" +
                    "## Evolución tras responder la pregunta (Hibridación Local/Remota)\n" +
                    "* **Procesamiento en Dos Etapas:** La conversión de voz a texto se desacopla completamente de la estructuración semántica. La inferencia local de `Whisper.tflite` genera un String intermedio de manera instantánea y offline.\n" +
                    "* **Estrategia de Tolerancia a Fallos:** Si la llamada al servicio de estructuración remota falla por falta de conectividad, el texto plano recuperado por el hardware local no se descarta; se almacena en Room bajo un estado de `PENDING_STRUCTURE`. El Repositorio coordinará la petición asíncrona en la nube una vez que el estado de red pase a activo, garantizando que el usuario pueda seguir volcando ideas aunque el servidor Express o la API de IA no estén accesibles.\n" +
                    "\n" +
                    "## Arquitectura Técnica (Clean Architecture)\n" +
                    "* **Capa de Dominio:** Contiene los modelos puros (como `ProcessedIdeaDraft`) y las reglas de negocio, totalmente aislada de librerías externas.\n" +
                    "* **Capa de Data (Persistencia y Red):**\n" +
                    "    * **Room:** Almacenamiento local para garantizar el funcionamiento sin conexión y soporte para estados de sincronización diferida (`PENDING_STRUCTURE`).\n" +
                    "    * **Retrofit:** Conexión con el servidor Express para la gestión de usuarios y respaldo en la nube.\n" +
                    "    * **SpeechToTextDataSource:** Componente local que gestiona el motor de inferencia en el chip del dispositivo mediante TFLite para obtener el texto crudo.\n" +
                    "    * **IdeaProcessorDataSource:** Abstracción para el procesamiento, resumen y generación de preguntas semánticas en la nube."
        )
    }
}
