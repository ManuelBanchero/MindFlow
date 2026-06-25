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
import com.example.mindflow.ui.presentation.createidea.CreateIdeaScreen
import com.example.mindflow.ui.presentation.ideadetail.IdeaDetailScreen
import com.example.mindflow.ui.presentation.idealist.IdeaListScreen
import com.example.mindflow.ui.presentation.login.LoginScreen
import com.example.mindflow.ui.presentation.questions.QuestionsScreen
import com.example.mindflow.ui.presentation.updateidea.UpdateIdeaScreen
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
                    navController.navigate("create_idea") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel = hiltViewModel()
            )
        }

        composable("create_idea") {
            CreateIdeaScreen(
                onCreateIdeaSuccess = {
                    navController.navigate("idea_list") {
                        popUpTo("create_idea") { inclusive = true }
                    }
                },
                onNavigateToIdeaList = {
                    navController.navigate("idea_list") {
                        launchSingleTop = true
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
                onNavigateToCreateIdea = {
                    navController.navigate("create_idea") {
                        launchSingleTop = true
                    }
                },
                viewModel = hiltViewModel()
            )
        }

        // Pantalla de Detalle (Placeholder por ahora)
        composable(
            route = "idea_detail/{ideaId}",
            arguments = listOf(navArgument("ideaId") { type = NavType.IntType })
        ) {
            IdeaDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = { ideaId ->
                    navController.navigate("update_idea/$ideaId")
                },
                onNavigateToQuestions = { ideaId ->
                    navController.navigate("questions/$ideaId")
                },
                onDeleted = {
                    navController.popBackStack()
                },
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = "questions/{ideaId}",
            arguments = listOf(navArgument("ideaId") { type = NavType.IntType })
        ) {
            QuestionsScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToCreateIdea = {
                    navController.navigate("create_idea") {
                        launchSingleTop = true
                    }
                },
                onNavigateToIdeaList = {
                    navController.navigate("idea_list") {
                        launchSingleTop = true
                    }
                },
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = "update_idea/{ideaId}",
            arguments = listOf(navArgument("ideaId") { type = NavType.IntType })
        ) {
            UpdateIdeaScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                },
                viewModel = hiltViewModel()
            )
        }
    }
}
