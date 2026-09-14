package com.leo.clean_mvvm_mvi.feature.auth.domain.usecase

data class AuthUseCases(
    val sendOtp: SendOtpUseCase,
    val loginWithBiometrics: BiometricLoginUseCase
)

