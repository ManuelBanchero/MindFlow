package com.example.mindflow.ui.presentation.createidea

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.example.mindflow.domain.service.RecordingState
import com.example.mindflow.ui.components.MindFlowBackground
import com.example.mindflow.ui.components.MindFlowCircularActionButton
import com.example.mindflow.ui.theme.mindFlowColors

private const val WAVE_LOTTIE_ASSET = "animations/animation_water.json"
//private const val WAVE_LOTTIE_ASSET = "animations/wave_animation_2.json"

@Composable
fun CreateIdeaContent(
    uiState: CreateIdeaUiState,
    onEvent: (CreateIdeaEvent) -> Unit,
    onNavigateToIdeaList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onEvent(CreateIdeaEvent.OnToggleRecord)
            }
        }
    )

    val onPrimaryAction = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            onEvent(CreateIdeaEvent.OnToggleRecord)
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    CreateIdeaContentLayout(
        uiState = uiState,
        onPrimaryAction = onPrimaryAction,
        onPauseAction = { onEvent(CreateIdeaEvent.OnTogglePause) },
        onCancelAction = { onEvent(CreateIdeaEvent.OnCancelIdea) },
        onNavigateToIdeaList = onNavigateToIdeaList,
        modifier = modifier
    )
}

@Composable
private fun CreateIdeaContentLayout(
    uiState: CreateIdeaUiState,
    onPrimaryAction: () -> Unit,
    onPauseAction: () -> Unit,
    onCancelAction: () -> Unit,
    onNavigateToIdeaList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.mindFlowColors
    val isDark = isSystemInDarkTheme()
    val isActive = uiState.recordingState is RecordingState.Recording ||
        uiState.recordingState is RecordingState.Paused
    val isRecording = uiState.recordingState is RecordingState.Recording
    val isPaused = uiState.recordingState is RecordingState.Paused
    val title = when {
        uiState.isProcessing -> "Guardando tu idea"
        isRecording -> "Te estoy escuchando"
        isPaused -> "Grabación pausada"
        uiState.recordingState is RecordingState.Error -> "Algo no salió bien"
        else -> "¿Listo para soltar tus ideas?"
    }
    val subtitle = when {
        uiState.isProcessing -> "Convirtiendo tu voz en una idea organizada."
        isRecording -> "Cuando termines, tocá finalizar."
        isPaused -> "Podés continuar grabando o cancelar esta captura."
        uiState.recordingState is RecordingState.Error -> "Revisá el permiso del micrófono e intentá de nuevo."
        else -> "Presioná el micrófono y comenzá a hablar."
    }

    CreateIdeaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 430.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                CreateIdeaTopBar()

                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "Hola Manuel,",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isDark) MaterialTheme.colorScheme.onBackground else colors.textLabel,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (isDark) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isDark) {
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.78f)
                    } else {
                        colors.textSecondary
                    },
                    fontWeight = if (isDark) FontWeight.SemiBold else FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(46.dp))

                VoiceOrb(
                    isRecording = isRecording,
                    isPaused = isPaused,
                    isProcessing = uiState.isProcessing
                )

                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(22.dp))
                    CreateIdeaErrorMessage(uiState.error)
                }

                Spacer(modifier = Modifier.weight(1f))

                if (isActive) {
                    ActiveRecordingControls(
                        isPaused = isPaused,
                        enabled = !uiState.isProcessing,
                        onPauseAction = onPauseAction,
                        onCancelAction = onCancelAction
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }

                CaptureButton(
                    isActive = isActive,
                    isProcessing = uiState.isProcessing,
                    onClick = onPrimaryAction
                )

                Spacer(modifier = Modifier.height(28.dp))

                CreateIdeaBottomNavigation(
                    onNavigateToIdeaList = onNavigateToIdeaList
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun CreateIdeaBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    if (!isDark) {
        MindFlowBackground(modifier = modifier) {
            content()
        }
        return
    }

    val isPreview = LocalInspectionMode.current
    val backgroundColor = MaterialTheme.colorScheme.background
    val glowAlpha = if (isPreview) 0.08f else 0.055f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                        MaterialTheme.colorScheme.background.copy(alpha = 0f)
                    ),
                    radius = 760f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CreateIdeaTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TopIconButton(
            icon = Icons.Default.Menu,
            contentDescription = "Abrir menú",
            onClick = { }
        )

        TopIconButton(
            icon = Icons.Default.Person,
            contentDescription = "Perfil",
            onClick = { }
        )
    }
}

@Composable
private fun TopIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Surface(
        onClick = onClick,
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        color = if (isDark) {
            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.72f)
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.74f)
        },
        contentColor = if (isDark) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.primary
        }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun VoiceOrb(
    isRecording: Boolean,
    isPaused: Boolean,
    isProcessing: Boolean
) {
    val animationSpeed = if (isRecording) 2f else 0.5f

    Box(
        modifier = Modifier.size(284.dp),
        contentAlignment = Alignment.Center
    ) {
        DotLottieAnimation(
            modifier = Modifier.fillMaxSize(),
            source = DotLottieSource.Asset(WAVE_LOTTIE_ASSET),
            autoplay = true,
            loop = true,
            speed = animationSpeed,
            useFrameInterpolation = false,
            playMode = Mode.FORWARD
        )
    }
}

@Composable
private fun ActiveRecordingControls(
    isPaused: Boolean,
    enabled: Boolean,
    onPauseAction: () -> Unit,
    onCancelAction: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SecondaryRecordAction(
            text = if (isPaused) "Continuar" else "Pausar",
            icon = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
            enabled = enabled,
            onClick = onPauseAction
        )

        SecondaryRecordAction(
            text = "Cancelar",
            icon = Icons.Default.Close,
            enabled = enabled,
            danger = true,
            onClick = onCancelAction
        )
    }
}

@Composable
private fun SecondaryRecordAction(
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    danger: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = if (danger) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CaptureButton(
    isActive: Boolean,
    isProcessing: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    MindFlowCircularActionButton(
        onClick = onClick,
        enabled = !isProcessing,
        isLoading = isProcessing,
        icon = if (isActive) Icons.Default.Stop else Icons.Default.Mic,
        contentDescription = if (isActive) "Finalizar grabación" else "Comenzar grabación",
        containerColor = if (isDark) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
        contentColor = if (isDark) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
private fun CreateIdeaBottomNavigation(
    onNavigateToIdeaList: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = if (isDark) {
            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.78f)
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.74f)
        },
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                text = "Capturar",
                icon = Icons.Default.Mic,
                selected = true,
                onClick = { }
            )
            BottomNavItem(
                text = "Historial",
                icon = Icons.Default.History,
                selected = false,
                onClick = onNavigateToIdeaList
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.mindFlowColors
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        colors.textSecondary
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CreateIdeaErrorMessage(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
