package com.leo.clean_mvvm_mvi.feature.auth.ui

import androidx.compose.runtime.Immutable
import com.leo.clean_mvvm_mvi.core.ui.UiState

@Immutable
data class LoginState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val isDarkMode: Boolean = true,
    val selectedLanguage: String = "EN",
    val errorMessage: String? = null
) : UiState

