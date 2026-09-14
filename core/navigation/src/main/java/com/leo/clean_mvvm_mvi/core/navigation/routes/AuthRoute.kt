package com.leo.clean_mvvm_mvi.core.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRoute {
    @Serializable
    data object Root : AuthRoute

    @Serializable
    data object Login : AuthRoute

    @Serializable
    data object Register : AuthRoute

    @Serializable
    data class ForgotPassword(val initialEmail: String? = null) : AuthRoute
}
