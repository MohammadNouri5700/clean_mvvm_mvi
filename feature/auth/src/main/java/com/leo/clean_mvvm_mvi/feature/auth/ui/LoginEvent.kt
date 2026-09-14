package com.leo.clean_mvvm_mvi.feature.auth.ui

import com.leo.clean_mvvm_mvi.core.designsystem.component.EdgeNotificationType
import com.leo.clean_mvvm_mvi.core.ui.UiEvent

sealed interface LoginEvent : UiEvent {
    data class ShowSnackbar(
        val message: String,
        val type: EdgeNotificationType = EdgeNotificationType.INFO
    ) : LoginEvent
    data class Navigate(val route: Any) : LoginEvent
}
