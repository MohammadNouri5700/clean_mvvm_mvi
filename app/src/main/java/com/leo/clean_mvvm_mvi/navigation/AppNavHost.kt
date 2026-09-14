package com.leo.clean_mvvm_mvi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.leo.clean_mvvm_mvi.core.navigation.routes.AuthRoute
import com.leo.clean_mvvm_mvi.feature.auth.navigation.authGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.Root,
        modifier = modifier
    ) {
        authGraph(
            onNavigateToHome = {
                // Navigation callback after successful authentication
            }
        )
    }
}
