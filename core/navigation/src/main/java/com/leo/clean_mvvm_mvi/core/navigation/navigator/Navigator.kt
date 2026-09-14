package com.leo.clean_mvvm_mvi.core.navigation.navigator

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface NavigationCommand {
    data class NavigateTo(
        val destination: Any,
        val popUpToDestination: Any? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = true
    ) : NavigationCommand

    data object PopBackStack : NavigationCommand
}

interface Navigator {
    val navigationCommands: SharedFlow<NavigationCommand>

    suspend fun navigateTo(
        destination: Any,
        popUpToDestination: Any? = null,
        inclusive: Boolean = false,
        singleTop: Boolean = true
    )

    suspend fun popBackStack()
}

@Singleton
class AppNavigator @Inject constructor() : Navigator {
    private val _navigationCommands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = 1)
    override val navigationCommands: SharedFlow<NavigationCommand> = _navigationCommands.asSharedFlow()

    override suspend fun navigateTo(
        destination: Any,
        popUpToDestination: Any?,
        inclusive: Boolean,
        singleTop: Boolean
    ) {
        _navigationCommands.emit(
            NavigationCommand.NavigateTo(
                destination = destination,
                popUpToDestination = popUpToDestination,
                inclusive = inclusive,
                singleTop = singleTop
            )
        )
    }

    override suspend fun popBackStack() {
        _navigationCommands.emit(NavigationCommand.PopBackStack)
    }
}
