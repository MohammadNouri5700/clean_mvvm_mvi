package com.leo.clean_mvvm_mvi.core.location.domain.repository

import com.leo.clean_mvvm_mvi.core.domain.result.DomainError
import com.leo.clean_mvvm_mvi.core.domain.result.Result
import com.leo.clean_mvvm_mvi.core.location.domain.model.LocationPoint
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun observeLocationUpdates(): Flow<LocationPoint>
    suspend fun getCurrentLocation(): Result<LocationPoint, DomainError.LocationError>
    suspend fun startLocationService(): Result<Unit, DomainError.LocationError>
    suspend fun stopLocationService(): Result<Unit, DomainError.LocationError>
}
