package com.leo.clean_mvvm_mvi.feature.auth.domain.di

import com.leo.clean_mvvm_mvi.feature.auth.domain.repository.AuthRepository
import com.leo.clean_mvvm_mvi.feature.auth.domain.usecase.AuthUseCases
import com.leo.clean_mvvm_mvi.feature.auth.domain.usecase.BiometricLoginUseCase
import com.leo.clean_mvvm_mvi.feature.auth.domain.usecase.SendOtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDomainModule {
    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            sendOtp = SendOtpUseCase(repository),
            loginWithBiometrics = BiometricLoginUseCase(repository),
        )
    }
}
