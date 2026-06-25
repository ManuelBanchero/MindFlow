package com.example.mindflow.ui.presentation.logout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mindflow.ui.components.MindFlowAuthCard
import com.example.mindflow.ui.components.MindFlowBackground
import com.example.mindflow.ui.components.MindFlowAuthHero
import com.example.mindflow.ui.components.MindFlowIconButton
import com.example.mindflow.ui.theme.mindFlowColors

@Composable
fun LogoutContent(
    uiState: LogoutUiState,
    onEvent: (LogoutEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.mindFlowColors

    MindFlowBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MindFlowIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        onClick = { onEvent(LogoutEvent.OnBackClick) },
                        size = 44.dp,
                        iconSize = 22.dp,
                        elevation = 8.dp,
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                MindFlowAuthHero(
                    title = "Tu perfil",
                    subtitle = "Revisá los datos de la sesión activa y cerrala cuando quieras."
                )

                Spacer(modifier = Modifier.height(28.dp))

                MindFlowAuthCard {
                    if (uiState.isLoading) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        Text(
                            text = "Información de cuenta",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Estos son los datos guardados para tu sesión activa.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        InfoRow(label = "Nombre", value = uiState.user?.firstName.orEmpty())
                        Spacer(modifier = Modifier.height(14.dp))
                        InfoRow(label = "Apellido", value = uiState.user?.lastName.orEmpty())
                        Spacer(modifier = Modifier.height(14.dp))
                        InfoRow(label = "Mail", value = uiState.user?.mail.orEmpty())

                        if (uiState.error != null) {
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = uiState.error,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = { onEvent(LogoutEvent.OnLogoutClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isLoading && !uiState.isLoggingOut,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.45f),
                        disabledContentColor = MaterialTheme.colorScheme.onError.copy(alpha = 0.65f)
                    )
                ) {
                    if (uiState.isLoggingOut) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 2.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null
                        )
                        Text(
                            text = "Cerrar sesión",
                            modifier = Modifier.padding(start = 10.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Al cerrar sesión vas a salir de tu cuenta actual y volver al inicio.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (uiState.showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(LogoutEvent.OnDismissDialog) },
            title = {
                Text(
                    text = "¿Querés cerrar sesión?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Vas a salir de tu cuenta actual y volver a la pantalla de inicio.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onEvent(LogoutEvent.OnConfirmLogout) },
                    enabled = !uiState.isLoggingOut
                ) {
                    Text(
                        text = "Cerrar sesión",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onEvent(LogoutEvent.OnDismissDialog) },
                    enabled = !uiState.isLoggingOut
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.mindFlowColors.textLabel,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
