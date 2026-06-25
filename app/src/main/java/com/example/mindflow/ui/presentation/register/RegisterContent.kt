package com.example.mindflow.ui.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mindflow.ui.components.MindFlowAuthCard
import com.example.mindflow.ui.components.MindFlowAuthErrorMessage
import com.example.mindflow.ui.components.MindFlowAuthHero
import com.example.mindflow.ui.components.MindFlowBackground
import com.example.mindflow.ui.components.MindFlowButton
import com.example.mindflow.ui.components.MindFlowTextField
import com.example.mindflow.ui.theme.mindFlowColors

@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val colors = MaterialTheme.mindFlowColors
    val isFormReady = uiState.firstName.isNotBlank() &&
        uiState.lastName.isNotBlank() &&
        uiState.mail.isNotBlank() &&
        uiState.password.isNotBlank()

    MindFlowBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 430.dp)
            ) {
                Spacer(modifier = Modifier.height(52.dp))

                MindFlowAuthHero(
                    title = "Crea tu cuenta",
                    subtitle = "Unite a todos los que ya decidieron dejar de procrastinar y pasar a la acción al darle vida a sus ideas"
                )

                Spacer(modifier = Modifier.height(42.dp))

                MindFlowAuthCard {
                    Text(
                        text = "Registro",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Completá tus datos para empezar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MindFlowTextField(
                        value = uiState.firstName,
                        onValueChange = { onEvent(RegisterEvent.FirstNameChanged(it)) },
                        label = "Nombre",
                        placeholder = "Tu nombre",
                        enabled = !uiState.isLoading,
                        leadingIcon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    MindFlowTextField(
                        value = uiState.lastName,
                        onValueChange = { onEvent(RegisterEvent.LastNameChanged(it)) },
                        label = "Apellido",
                        placeholder = "Tu apellido",
                        enabled = !uiState.isLoading,
                        leadingIcon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    MindFlowTextField(
                        value = uiState.mail,
                        onValueChange = { onEvent(RegisterEvent.MailChanged(it)) },
                        label = "Mail",
                        placeholder = "nombre@ejemplo.com",
                        enabled = !uiState.isLoading,
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    MindFlowTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                        label = "Contraseña",
                        placeholder = "Ingresá tu contraseña",
                        enabled = !uiState.isLoading,
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        onTrailingIconClick = { passwordVisible = !passwordVisible },
                        trailingIconContentDescription = if (passwordVisible) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(18.dp))
                        MindFlowAuthErrorMessage(message = uiState.error)
                    } else {
                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    MindFlowButton(
                        text = "Registrate",
                        onClick = { onEvent(RegisterEvent.OnRegisterClick) },
                        isLoading = uiState.isLoading,
                        enabled = isFormReady,
                        icon = Icons.Default.Check
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.ctaText,
                        disabledContentColor = colors.textSecondary
                    )
                ) {
                    Text(
                        text = "¿Ya tenés cuenta? Iniciá sesión",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}
