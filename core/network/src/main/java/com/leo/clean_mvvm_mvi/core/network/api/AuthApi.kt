package com.leo.clean_mvvm_mvi.core.network.api

import com.leo.clean_mvvm_mvi.core.network.model.Driver
import com.leo.clean_mvvm_mvi.core.network.model.LoginRequest
import com.leo.clean_mvvm_mvi.core.network.model.OAuth
import com.leo.clean_mvvm_mvi.core.network.model.OtpResponse
import com.leo.clean_mvvm_mvi.core.network.model.RefreshTokenRequest
import com.leo.clean_mvvm_mvi.core.network.model.RefreshTokenResponse
import com.leo.clean_mvvm_mvi.core.network.model.SendOtpRequest
import com.leo.clean_mvvm_mvi.core.network.model.VerifyOtpRequest
import com.leo.clean_mvvm_mvi.core.network.model.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("driver/otp/send/")
    suspend fun sendOtp(
        @Body request: SendOtpRequest,
    ): OtpResponse

    @POST("driver/otp/verify/")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest,
    ): VerifyOtpResponse

    @POST("driver/account/api/auth/login/")
    suspend fun login(
        @Body request: LoginRequest,
    ): OAuth

    @GET("driver/account/api/account/profile/")
    suspend fun profile(): Driver

    @POST("driver/token/refresh/")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest,
    ): RefreshTokenResponse

    @POST("driver/logout/")
    suspend fun logout(
        @Body request: RefreshTokenRequest,
    ): Response<Unit>
}

