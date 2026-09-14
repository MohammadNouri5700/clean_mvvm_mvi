package com.leo.clean_mvvm_mvi.core.network.api

import com.leo.clean_mvvm_mvi.core.network.model.DriverStatusDto
import com.leo.clean_mvvm_mvi.core.network.model.UpdateStatusRequest
import com.leo.clean_mvvm_mvi.core.network.model.UpdateStatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DriverApiService {
    @GET("api/v1/driver/status")
    suspend fun getDriverStatus(): Response<DriverStatusDto>

    @POST("api/v1/driver/status")
    suspend fun updateStatus(
        @Body request: UpdateStatusRequest
    ): Response<UpdateStatusResponse>
}

