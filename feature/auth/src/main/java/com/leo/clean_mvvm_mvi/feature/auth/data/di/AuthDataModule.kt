package com.leo.clean_mvvm_mvi.feature.auth.data.di

import com.leo.clean_mvvm_mvi.feature.auth.data.repository.AuthRepositoryImpl
import com.leo.clean_mvvm_mvi.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository
}
