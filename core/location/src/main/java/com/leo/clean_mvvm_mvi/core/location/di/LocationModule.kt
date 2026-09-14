package com.leo.clean_mvvm_mvi.core.location.di

import com.leo.clean_mvvm_mvi.core.location.data.repository.LocationRepositoryImpl
import com.leo.clean_mvvm_mvi.core.location.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}
