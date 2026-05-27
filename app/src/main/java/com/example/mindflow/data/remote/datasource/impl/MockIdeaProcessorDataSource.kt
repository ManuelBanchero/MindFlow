package com.example.mindflow.data.remote.datasource.impl

import com.example.mindflow.data.remote.datasource.IdeaProcessorDataSource
import com.example.mindflow.data.remote.dto.ProcessedAnswerQuestionDTO
import com.example.mindflow.data.remote.dto.ProcessedIdeaDraftDTO
import com.example.mindflow.data.remote.dto.ProcessedQuestionDraftDTO
import kotlinx.coroutines.delay
import kotlin.random.Random

class MockIdeaProcessorDataSourceImpl : IdeaProcessorDataSource {

    override suspend fun processRawText(text: String): ProcessedIdeaDraftDTO {
        // Simulating an API response
        delay(2000)

        val mockResponses = listOf(
            // Opción 0: Temática MindFlow
            ProcessedIdeaDraftDTO(
                title = "Arquitectura Limpia y Persistencia en MindFlow",
                category = "Desarrollo Mobile / Android",
                summarizeContent = "Implementación de la capa de Data en Android usando Room para local, Retrofit para Express, y un DataSource independiente para el procesamiento de texto con IA.",
                structuredIdea = """
                    ## Cimientos del Proyecto:
                    1. **Modularización de Capas:** Dominio (puro), Data (infraestructura) y UI (Compose).
                    2. **Flujo Asincrónico:** Uso de funciones 'suspend' para no bloquear el hilo principal.
                """.trimIndent(),
                questions = listOf(
                    ProcessedQuestionDraftDTO(
                        category = "Arquitectura",
                        questionText = "¿Por qué el Result de Kotlin no debe usarse en los DataSources?",
                        description = "Analizar el manejo de excepciones de infraestructura frente a las de negocio."
                    ),
                    ProcessedQuestionDraftDTO(
                        category = "Persistencia",
                        questionText = "¿Qué diferencia hay entre capturar una IOException y una HttpException?",
                        description = "Evaluar la respuesta ante la falta de conexión física vs rechazo del servidor."
                    )
                )
            ),

            // Opción 1: Temática Estructuras de Datos
            ProcessedIdeaDraftDTO(
                title = "Optimización de Búsquedas Locales con Estructuras Trie",
                category = "Ciencias de la Computación",
                summarizeContent = "Análisis del uso de árboles de prefijo (Tries) para la indexación y recuperación eficiente de texto en bases de datos locales, ideal para buscadores instantáneos.",
                structuredIdea = """
                    ## Conceptos Clave de un Trie:
                    1. **Nodo Raíz:** Punto de partida vacío de la estructura de búsqueda.
                    2. **Caminos de Caracteres:** Cada arista representa una letra, optimizando el espacio al compartir prefijos comunes.
                    3. **Complejidad Temporal:** Las búsquedas toman un tiempo de O(L), donde L es la longitud de la palabra, siendo independiente del tamaño del diccionario.
                """.trimIndent(),
                questions = listOf(
                    ProcessedQuestionDraftDTO(
                        category = "Algoritmos",
                        questionText = "¿Cómo se gestiona el borrado de una palabra en un Trie sin romper otros prefijos?",
                        description = "Evaluar el comportamiento del algoritmo recursivo al limpiar nodos huérfanos."
                    ),
                    ProcessedQuestionDraftDTO(
                        category = "Memoria",
                        questionText = "¿Cuál es el costo en memoria de un Trie frente a un HashMap tradicional?",
                        description = "Comparar la dispersión de punteros en memoria con el almacenamiento continuo indexado."
                    )
                )
            ),

            // Opción 2: Temática Productividad / Entornos de Desarrollo
            ProcessedIdeaDraftDTO(
                title = "Automatización de Workflows y Configuración Eficiente de Neovim",
                category = "Productividad / Herramientas",
                summarizeContent = "Estudio del ecosistema de Neovim usando Lua para el desarrollo ágil de software, destacando la gestión de registros, keybindings y buffers para evitar el uso del mouse.",
                structuredIdea = """
                    ## Pilares de la Productividad en la Terminal:
                    1. **Modalidad Estricta:** Navegación pura con hjkl para eliminar tiempos muertos de traslado de la mano.
                    2. **Registros de Memoria:** Uso avanzado de buffers intermedios para manipular múltiples bloques de texto en paralelo.
                    3. **Ecosistema de Plugins:** Integración de herramientas como Telescope para búsquedas instantáneas en proyectos masivos.
                """.trimIndent(),
                questions = listOf(
                    ProcessedQuestionDraftDTO(
                        category = "Flujo de Trabajo",
                        questionText = "¿Cómo impacta la memorización de atajos de teclado en la carga cognitiva del programador?",
                        description = "Analizar la transición del esfuerzo consciente a la memoria muscular mecánica."
                    )
                )
            )
        )

        val randomIndex = Random.nextInt(mockResponses.size)
        return mockResponses[randomIndex]
    }

    override suspend fun expandIdeaWithNewContext(
        ideaTitle: String,
        ideaContent: String,
        newContext: String
    ): ProcessedIdeaDraftDTO {
        // Simulating an API response
        delay(2000)

        val mockExtendedResponses = listOf(
            // Opción 0: Temática MindFlow (Extendida por voz)
            ProcessedIdeaDraftDTO(
                title = "Arquitectura Limpia y Persistencia en MindFlow",
                category = "Desarrollo Mobile / Android",
                summarizeContent = "Implementación de la capa de Data en Android usando Room para local, Retrofit para Express, y un DataSource independiente para el procesamiento de texto con IA.",
                structuredIdea = """
            ## Cimientos del Proyecto:
            1. **Modularización de Capas:** Dominio (puro), Data (infraestructura) y UI (Compose).
            2. **Flujo Asincrónico:** Uso de funciones 'suspend' para no bloquear el hilo principal.
            
            ## Extensión por Voz (Nuevos pensamientos del usuario):
            3. **Manejo Dinámico de Errores:** Se profundizó en que los DataSources no deben capturar el éxito/fallo con wrappers de negocio. Deben propagar excepciones puras para que el Repositorio decida la estrategia de recuperación (como activar el modo offline de Room de forma transparente).
            4. **Estrategia Anticaídas:** Si el guardado local en la SQLite falla tras un registro exitoso en Express, no se revierte el estado en la nube; se fuerza un reintento de sincronización en el próximo inicio de sesión.
        """.trimIndent(),
                questions = listOf(
                    ProcessedQuestionDraftDTO(
                        category = "Arquitectura",
                        questionText = "¿Por qué el Result de Kotlin no debe usarse en los DataSources?",
                        description = "Analizar el manejo de excepciones de infraestructura frente a las de negocio."
                    ),
                    ProcessedQuestionDraftDTO(
                        category = "Persistencia",
                        questionText = "¿Qué diferencia hay entre capturar una IOException y una HttpException?",
                        description = "Evaluar la respuesta ante la falta de conexión física vs rechazo del servidor."
                    ),
                    ProcessedQuestionDraftDTO(
                        category = "Flujo Alternativo",
                        questionText = "¿Cómo mitigar la desincronización si Room falla y Express da OK?",
                        description = "Diseñar la lógica de conciliación de datos durante el flujo de inicio de sesión defensivo."
                    )
                )
            ),

            // Opción 1: Temática Estructuras de Datos (Extendida por voz)
            ProcessedIdeaDraftDTO(
                title = "Optimización de Búsquedas Locales con Estructuras Trie",
                category = "Ciencias de la Computación",
                summarizeContent = "Análisis del uso de árboles de prefijo (Tries) para la indexación y recuperación eficiente de texto en bases de datos locales, ideal para buscadores instantáneos.",
                structuredIdea = """
            ## Conceptos Clave de un Trie:
            1. **Nodo Raíz:** Punto de partida vacío de la estructura de búsqueda.
            2. **Caminos de Caracteres:** Cada arista representa una letra, optimizando el espacio al compartir prefijos comunes.
            3. **Complejidad Temporal:** Las búsquedas toman un tiempo de O(L), donde L es la longitud de la palabra.
            
            ## Extensión por Voz (Nuevos pensamientos del usuario):
            4. **Integración con Frameworks UI:** El usuario plantea exportar esta lógica de ordenamiento a un componente visual nativo usando Tauri o Electron. Se analiza el impacto de serializar el árbol Trie a través de un puente IPC (Inter-Process Communication) para renderizar un buscador global de Notion en tiempo real.
        """.trimIndent(),
                questions = listOf(
                    ProcessedQuestionDraftDTO(
                        category = "Algoritmos",
                        questionText = "¿Cómo se gestiona el borrado de una palabra en un Trie sin romper otros prefijos?",
                        description = "Evaluar el comportamiento del algoritmo recursivo al limpiar nodos huérfanos."
                    ),
                    ProcessedQuestionDraftDTO(
                        category = "Memoria",
                        questionText = "¿Cuál es el costo en memoria de un Trie frente a un HashMap tradicional?",
                        description = "Comparar la dispersión de punteros en memoria con el almacenamiento continuo indexado."
                    ),
                    ProcessedQuestionDraftDTO(
                        category = "Interoperabilidad",
                        questionText = "¿Cómo afecta el cuello de botella del IPC al transferir un Trie entre Rust/C y la UI?",
                        description = "Estudiar la latencia de serialización al enviar estructuras jerárquicas complejas a entornos web."
                    )
                )
            ),

            // Opción 2: Temática Productividad / Entornos de Desarrollo (Extendida por voz)
            ProcessedIdeaDraftDTO(
                title = "Automatización de Workflows y Configuración Eficiente de Neovim",
                category = "Productividad / Herramientas",
                summarizeContent = "Estudio del ecosistema de Neovim usando Lua para el desarrollo ágil de software, destacando la gestión de registros, keybindings y buffers para evitar el uso del mouse.",
                structuredIdea = """
            ## Pilares de la Productividad en la Terminal:
            1. **Modalidad Estricta:** Navegación pura con hjkl para eliminar tiempos muertos de traslado de la mano.
            2. **Registros de Memoria:** Uso avanzado de buffers intermedios para manipular múltiples bloques de texto en paralelo.
            3. **Ecosistema de Plugins:** Integración de herramientas como Telescope para búsquedas instantáneas.
            
            ## Extensión por Voz (Nuevos pensamientos del usuario):
            4. **Mapeos Contextuales con Lua:** El usuario agrega la necesidad de crear macros específicas que interactúen con la API de Notion directamente desde un buffer de Neovim, permitiendo guardar notas de texto plano y transformarlas automáticamente en bloques de bases de datos remotas mediante atajos personalizados.
        """.trimIndent(),
                questions = listOf(
                    ProcessedQuestionDraftDTO(
                        category = "Flujo de Trabajo",
                        questionText = "¿Cómo impacta la memorización de atajos de teclado en la carga cognitiva del programador?",
                        description = "Analizar la transición del esfuerzo consciente a la memoria muscular mecánica."
                    ),
                    ProcessedQuestionDraftDTO(
                        category = "Integraciones",
                        questionText = "¿Cómo mitigar la latencia de la API de Notion en llamadas síncronas desde Neovim?",
                        description = "Diseñar un sistema de encolamiento asíncrono en Lua utilizando jobs nativos de Neovim."
                    )
                )
            )
        )

        val randomIndex = Random.nextInt(mockExtendedResponses.size)
        return mockExtendedResponses[randomIndex]
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

        val mockAnsweredResponses = listOf(
            // Opción 0: Respuesta a la pregunta de MindFlow (Sobre el Result en DataSources)
            ProcessedAnswerQuestionDTO(
                summarizeContent = "Actualización del flujo de datos en MindFlow tras consolidar que los DataSources deben propagar excepciones crudas (HttpException/IOException) en lugar de atraparlas en un wrapper Result. Esto centraliza la lógica de reintentos y el manejo offline exclusivamente en el Repositorio.",
                structuredIdea = """
            ## Cimientos del Proyecto:
            1. **Modularización de Capas:** Dominio (puro), Data (infraestructura) y UI (Compose).
            2. **Flujo Asincrónico:** Uso de funciones 'suspend' para no bloquear el hilo principal.
            
            ## Evolución tras responder la pregunta (Aislamiento de Errores):
            * **Ds sin Wrappers:** Se eliminó el uso de 'Result' del `UserRemoteDataSource`. Ahora las excepciones vuelan directamente hacia el `UserRepositoryImpl`.
            * **Control de Flujo:** El Repositorio ataja el fallo mediante bloques 'try-catch' específicos. Si es un error 409 de Express, inyecta una excepción de Dominio personalizada; si es un fallo de Room, maneja la persistencia de forma aislada, garantizando un flujo robusto y testeable.
        """.trimIndent()
            ),

            // Opción 1: Respuesta a la pregunta de Estructuras de Datos (Sobre el borrado en un Trie)
            ProcessedAnswerQuestionDTO(
                summarizeContent = "Análisis optimizado de la estructura Trie local, detallando la implementación de un algoritmo de borrado recursivo que limpia nodos intermedios huérfanos sin alterar los prefijos de otras palabras indexadas de forma simultánea.",
                structuredIdea = """
            ## Conceptos Clave de un Trie:
            1. **Nodo Raíz:** Punto de partida vacío de la estructura de búsqueda.
            2. **Caminos de Caracteres:** Cada arista representa una letra, optimizando el espacio al compartir prefijos comunes.
            
            ## Evolución tras responder la pregunta (Mecánica de Borrado):
            * **Algoritmo de Desindexación:** El borrado se ejecuta de abajo hacia arriba (post-order). Se remueve el flag de 'fin de palabra' del nodo terminal.
            * **Limpieza de Memoria:** Si el nodo final no tiene hijos activos, se elimina del mapa del padre de forma recursiva. El retroceso se detiene inmediatamente al encontrar un nodo que pertenezca a otra palabra o que contenga ramificaciones adicionales, evitando la pérdida de datos concurrentes.
        """.trimIndent()
            ),

            // Opción 2: Respuesta a la pregunta de Neovim (Sobre la latencia de la API de Notion)
            ProcessedAnswerQuestionDTO(
                summarizeContent = "Optimización del workflow en Neovim mediante Lua, incorporando un sistema de Jobs asíncronos en segundo plano para mitigar la latencia de la API de Notion al exportar notas técnicas sin congelar el editor de texto.",
                structuredIdea = """
            ## Pilares de la Productividad en la Terminal:
            1. **Modalidad Estricta:** Navegación pura con hjkl para eliminar tiempos muertos de traslado de la mano.
            2. **Registros de Memoria:** Uso avanzado de buffers intermedios para manipular múltiples bloques de texto en paralelo.
            
            ## Evolución tras responder la pregunta (Integración Asíncrona):
            * **Evitar Bloqueos del Editor:** En lugar de ejecutar llamadas curl síncronas que congelan el buffer de Lua, se implementó el uso de `vim.loop` (libuv) o `vim.fn.jobstart()`.
            * **Procesamiento en Background:** Los requests hacia la API de Notion corren en hilos separados del sistema operativo. Neovim recibe el estado de éxito o error a través de callbacks sin interrumpir la escritura en tiempo real del programador.
        """.trimIndent()
            )
        )

        val randomIndex = Random.nextInt(mockAnsweredResponses.size)
        return mockAnsweredResponses[randomIndex]
    }
}