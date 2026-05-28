package com.example.mindflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mindflow.ui.presentation.idealist.IdeaListScreen
import com.example.mindflow.ui.presentation.login.LoginScreen
import com.example.mindflow.ui.theme.MindFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindFlowTheme {
                MindFlowAppNavigation()
            }
        }
    }
}

@Composable
fun MindFlowAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("idea_list") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel = hiltViewModel()
            )
        }

        // Pantalla de Lista de Ideas
        composable("idea_list") {
            IdeaListScreen(
                onNavigateToIdeaDetail = { ideaId ->
                    navController.navigate("idea_detail/$ideaId")
                },
                viewModel = hiltViewModel()
            )
        }

        // Pantalla de Detalle (Placeholder por ahora)
        composable(
            route = "idea_detail/{ideaId}",
            arguments = listOf(navArgument("ideaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ideaId = backStackEntry.arguments?.getInt("ideaId") ?: 0
            // Aquí iría tu IdeaDetailScreen(ideaId)
        }
    }
}
