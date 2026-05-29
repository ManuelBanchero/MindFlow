package com.example.mindflow.ui.presentation.createidea

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CreateIdeaContent(
    uiState: CreateIdeaUiState,
    onEvent: (CreateIdeaEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isProcessing) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Procesando idea...")
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (uiState.isRecording) "Grabando..." else "Listo para grabar",
                    style = MaterialTheme.typography.headlineSmall
                )

                if (uiState.error != null) {
                    Text(text = uiState.error, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Principal: Grabar / Procesar
                IconButton(
                    onClick = {
                        if (uiState.isRecording) {
                            // Al detener, enviamos el path falso
                            onEvent(CreateIdeaEvent.OnToggleRecord("uri/fake_audio_path.mp3"))
                        } else {
                            onEvent(CreateIdeaEvent.OnToggleRecord())
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.isRecording) Icons.Default.Done else Icons.Default.PlayArrow,
                        contentDescription = if (uiState.isRecording) "Procesar" else "Grabar",
                        modifier = Modifier.size(48.dp),
                        tint = if (uiState.isRecording) Color.Green else MaterialTheme.colorScheme.primary
                    )
                }

                // Botones extra si está grabando
                if (uiState.isRecording) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Botón de Pausa
                        IconButton(onClick = { onEvent(CreateIdeaEvent.OnTogglePause) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh, // Usado como icono de toggle/pausa
                                contentDescription = if (uiState.isPaused) "Continuar" else "Pausar"
                            )
                        }

                        // Botón de Cancelar
                        IconButton(onClick = { onEvent(CreateIdeaEvent.OnCancelIdea) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancelar",
                                tint = Color.Red
                            )
                        }
                    }

                    if (uiState.isPaused) {
                        Text(text = "Grabación en pausa", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}