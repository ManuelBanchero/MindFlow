package com.example.mindflow.ui.presentation.createidea

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mindflow.domain.service.RecordingState

@Composable
fun CreateIdeaContent(
    uiState: CreateIdeaUiState,
    onEvent: (CreateIdeaEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Launcher para pedir el permiso de audio
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onEvent(CreateIdeaEvent.OnToggleRecord)
            }
        }
    )

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
                // Estado del texto basado en RecordingState
                val statusText = when (uiState.recordingState) {
                    is RecordingState.Idle -> "Listo para grabar"
                    is RecordingState.Recording -> "Grabando..."
                    is RecordingState.Paused -> "Grabación en pausa"
                    is RecordingState.Error -> "Error en la grabación"
                }

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.headlineSmall
                )

                if (uiState.error != null) {
                    Text(text = uiState.error, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Principal: Grabar / Procesar
                IconButton(
                    onClick = {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            onEvent(CreateIdeaEvent.OnToggleRecord)
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    val isRecording = uiState.recordingState is RecordingState.Recording || 
                                     uiState.recordingState is RecordingState.Paused
                    
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Done else Icons.Default.PlayArrow,
                        contentDescription = if (isRecording) "Procesar" else "Grabar",
                        modifier = Modifier.size(48.dp),
                        tint = if (isRecording) Color.Green else MaterialTheme.colorScheme.primary
                    )
                }

                // Botones extra si NO está inactivo
                if (uiState.recordingState !is RecordingState.Idle) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Botón de Pausa/Continuar
                        IconButton(onClick = { onEvent(CreateIdeaEvent.OnTogglePause) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = if (uiState.recordingState is RecordingState.Paused) "Continuar" else "Pausar"
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
                }
            }
        }
    }
}