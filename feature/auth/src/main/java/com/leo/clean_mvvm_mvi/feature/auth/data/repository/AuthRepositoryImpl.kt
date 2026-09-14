package com.leo.clean_mvvm_mvi.feature.auth.data.repository

import com.leo.clean_mvvm_mvi.core.domain.result.DomainError
import com.leo.clean_mvvm_mvi.core.domain.result.Result
import com.leo.clean_mvvm_mvi.core.network.api.AuthApi
import com.leo.clean_mvvm_mvi.core.network.model.LoginRequest
import com.leo.clean_mvvm_mvi.core.network.model.RefreshTokenRequest
import com.leo.clean_mvvm_mvi.core.network.model.SendOtpRequest
import com.leo.clean_mvvm_mvi.core.network.model.VerifyOtpRequest
import com.leo.clean_mvvm_mvi.feature.auth.data.mapper.toDomain
import com.leo.clean_mvvm_mvi.feature.auth.data.mapper.toDomainToken
import com.leo.clean_mvvm_mvi.feature.auth.domain.model.UserAuth
import com.leo.clean_mvvm_mvi.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
) : AuthRepository {

    override suspend fun sendOtp(phoneNumber: String): Result<Boolean, DomainError> {
        return try {
            val response = authApi.sendOtp(SendOtpRequest(phoneNumber))
            if ((response.status == "success") || (response.error == null)) {
                Result.Success(true)
            } else {
                Result.Error(DomainError.AuthError.InvalidCredentials)
            }
        } catch (e: Exception) {
            Result.Error(DomainError.NetworkError.NoInternet)
        }
    }

    override suspend fun verifyOtp(phoneNumber: String, otp: String): Result<UserAuth, DomainError> {
        return try {
            val response = authApi.verifyOtp(VerifyOtpRequest(phoneNumber, otp))
            val verifyData = response.data
            if (verifyData != null) {
                Result.Success(verifyData.toDomain())
            } else {
                Result.Error(DomainError.AuthError.InvalidCredentials)
            }
        } catch (e: Exception) {
            Result.Error(DomainError.NetworkError.NoInternet)
        }
    }

    override suspend fun login(username: String, password: String): Result<UserAuth, DomainError> {
        return try {
            val oAuthDto = authApi.login(LoginRequest(username, password))
            Result.Success(oAuthDto.toDomain())
        } catch (e: Exception) {
            Result.Error(DomainError.NetworkError.NoInternet)
        }
    }

    override suspend fun getProfile(): Result<UserAuth, DomainError> {
        return try {
            val driverDto = authApi.profile()
            Result.Success(driverDto.toDomain())
        } catch (e: Exception) {
            Result.Error(DomainError.NetworkError.NoInternet)
        }
    }

    override suspend fun refreshToken(refresh: String): Result<String, DomainError> {
        return try {
            val response = authApi.refreshToken(RefreshTokenRequest(refresh))
            val tokenData = response.data
            if (tokenData != null) {
                Result.Success(tokenData.toDomainToken())
            } else {
                Result.Error(DomainError.AuthError.InvalidCredentials)
            }
        } catch (e: Exception) {
            Result.Error(DomainError.NetworkError.NoInternet)
        }
    }

    override suspend fun logout(refresh: String): Result<Unit, DomainError> {
        return try {
            authApi.logout(RefreshTokenRequest(refresh))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainError.NetworkError.NoInternet)
        }
    }

    override suspend fun loginWithBiometrics(): Result<Boolean, DomainError> {
        return Result.Success(true)
    }
}
