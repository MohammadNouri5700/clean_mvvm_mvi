package com.leo.clean_mvvm_mvi.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.leo.clean_mvvm_mvi.core.navigation.routes.AuthRoute
import com.leo.clean_mvvm_mvi.feature.auth.ui.LoginScreen

fun NavGraphBuilder.authGraph(
    onNavigateToHome: () -> Unit
) {
    navigation<AuthRoute.Root>(startDestination = AuthRoute.Login) {
        composable<AuthRoute.Login> {
            LoginScreen(
                onNavigateToHome = onNavigateToHome
            )
        }
    }
}
