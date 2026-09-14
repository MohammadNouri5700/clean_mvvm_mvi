package com.leo.clean_mvvm_mvi.feature.auth.ui

import com.leo.clean_mvvm_mvi.core.ui.UiAction

sealed interface LoginAction : UiAction {
    data class PhoneNumberChanged(val phoneNumber: String) : LoginAction
    data object SendOtpClicked : LoginAction
    data object BiometricClicked : LoginAction
    data class ThemeToggled(val isDarkMode: Boolean) : LoginAction
    data object LanguageToggled : LoginAction
}

