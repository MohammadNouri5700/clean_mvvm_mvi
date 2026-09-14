package com.leo.clean_mvvm_mvi.core.navigation

import kotlinx.serialization.Serializable

sealed interface GlobalRoute {
    
    @Serializable
    data object AuthRoute : GlobalRoute {
        @Serializable
        data object Login : GlobalRoute
        @Serializable
        data object Registration : GlobalRoute
    }
}
