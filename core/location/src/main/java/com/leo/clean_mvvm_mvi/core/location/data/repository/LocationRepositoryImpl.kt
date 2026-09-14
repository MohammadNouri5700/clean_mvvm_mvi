package com.leo.clean_mvvm_mvi.core.location.data.repository

import com.leo.clean_mvvm_mvi.core.domain.result.DomainError
import com.leo.clean_mvvm_mvi.core.domain.result.Result
import com.leo.clean_mvvm_mvi.core.location.domain.model.LocationPoint
import com.leo.clean_mvvm_mvi.core.location.domain.repository.LocationRepository
import com.leo.clean_mvvm_mvi.core.location.provider.FusedLocationSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationSource: FusedLocationSource
) : LocationRepository {

    override fun observeLocationUpdates(): Flow<LocationPoint> {
        return fusedLocationSource.observeLocationUpdates()
    }

    override suspend fun getCurrentLocation(): Result<LocationPoint, DomainError.LocationError> {
        return try {
            val point = fusedLocationSource.observeLocationUpdates().first()
            Result.Success(point)
        } catch (e: Exception) {
            Result.Error(DomainError.LocationError.LocationDisabled)
        }
    }

    override suspend fun startLocationService(): Result<Unit, DomainError.LocationError> {
        return Result.Success(Unit)
    }

    override suspend fun stopLocationService(): Result<Unit, DomainError.LocationError> {
        return Result.Success(Unit)
    }
}
