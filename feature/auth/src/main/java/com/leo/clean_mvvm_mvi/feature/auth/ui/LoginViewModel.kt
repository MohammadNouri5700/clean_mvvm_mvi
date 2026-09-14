package com.leo.clean_mvvm_mvi.feature.auth.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leo.clean_mvvm_mvi.core.designsystem.component.EdgeNotificationType
import com.leo.clean_mvvm_mvi.core.domain.result.Result
import com.leo.clean_mvvm_mvi.core.navigation.routes.AuthRoute
import com.leo.clean_mvvm_mvi.core.ui.BaseViewModel
import com.leo.clean_mvvm_mvi.feature.auth.domain.usecase.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authUseCases: AuthUseCases
) : BaseViewModel<LoginState, LoginAction, LoginEvent>(
    savedStateHandle = savedStateHandle,
    initialState = LoginState()
) {

    override fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.PhoneNumberChanged -> {
                updateState { copy(phoneNumber = action.phoneNumber) }
            }
            LoginAction.SendOtpClicked -> {
                sendOtp()
            }
            LoginAction.BiometricClicked -> loginWithBiometrics()
            is LoginAction.ThemeToggled -> {
                updateState { copy(isDarkMode = action.isDarkMode) }
            }
            LoginAction.LanguageToggled -> {
                val newLang = if (currentState.selectedLanguage == "EN") "FA" else "EN"
                updateState { copy(selectedLanguage = newLang) }
            }
        }
    }

    private fun sendOtp() {
        if (currentState.phoneNumber.isBlank()) {
            sendEvent(
                LoginEvent.ShowSnackbar(
                    message = "لطفا شماره تلفن را وارد کنید",
                    type = EdgeNotificationType.WARNING
                )
            )
            return
        }
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (authUseCases.sendOtp(currentState.phoneNumber)) {
                is Result.Success -> {
                    updateState { copy(isLoading = false) }
                    sendEvent(LoginEvent.Navigate(AuthRoute.Root))
                }
                is Result.Error -> {
                    updateState { copy(isLoading = false, errorMessage = "خطا در ارسال کد تایید") }
                    sendEvent(
                        LoginEvent.ShowSnackbar(
                            message = "خطا در ارسال کد تایید",
                            type = EdgeNotificationType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun loginWithBiometrics() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            when (authUseCases.loginWithBiometrics()) {
                is Result.Success -> {
                    updateState { copy(isLoading = false) }
                    sendEvent(LoginEvent.Navigate(AuthRoute.Root))
                }
                is Result.Error -> {
                    updateState { copy(isLoading = false) }
                    sendEvent(
                        LoginEvent.ShowSnackbar(
                            message = "احراز هویت بیومتریک ناموفق بود",
                            type = EdgeNotificationType.ERROR
                        )
                    )
                }
            }
        }
    }
}
