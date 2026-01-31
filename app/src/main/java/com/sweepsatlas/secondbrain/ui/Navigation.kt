package com.sweepsatlas.secondbrain.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sweepsatlas.secondbrain.ui.screens.DocumentDetailScreen
import com.sweepsatlas.secondbrain.ui.screens.DocumentListScreen

sealed class Screen(val route: String) {
    object DocumentList : Screen("documents")
    object DocumentDetail : Screen("documents/{folder}/{slug}") {
        fun createRoute(folder: String, slug: String) = "documents/$folder/$slug"
    }
}

@Composable
fun SecondBrainNavHost() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.DocumentList.route
    ) {
        composable(Screen.DocumentList.route) {
            DocumentListScreen(
                onDocumentClick = { folder, slug ->
                    navController.navigate(Screen.DocumentDetail.createRoute(folder, slug))
                }
            )
        }
        
        composable(
            route = Screen.DocumentDetail.route,
            arguments = listOf(
                navArgument("folder") { type = NavType.StringType },
                navArgument("slug") { type = NavType.StringType }
            )
        ) {
            DocumentDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
