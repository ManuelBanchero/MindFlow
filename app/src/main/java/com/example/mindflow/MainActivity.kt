package com.example.mindflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mindflow.ui.presentation.login.LoginScreen
import com.example.mindflow.ui.theme.MindFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel // Importante

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindFlowTheme {
                LoginScreen(
                    onNavigateToMainPage = {
                        println("Navegación exitosa")
                    },
                    viewModel = hiltViewModel()
                )
            }
        }
    }
}

