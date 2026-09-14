package com.leo.clean_mvvm_mvi.feature.auth.domain.repository

import com.leo.clean_mvvm_mvi.core.domain.result.DomainError
import com.leo.clean_mvvm_mvi.core.domain.result.Result
import com.leo.clean_mvvm_mvi.feature.auth.domain.model.UserAuth

interface AuthRepository {
    suspend fun sendOtp(phoneNumber: String): Result<Boolean, DomainError>
    suspend fun verifyOtp(phoneNumber: String, otp: String): Result<UserAuth, DomainError>
    suspend fun login(username: String, password: String): Result<UserAuth, DomainError>
    suspend fun getProfile(): Result<UserAuth, DomainError>
    suspend fun refreshToken(refresh: String): Result<String, DomainError>
    suspend fun logout(refresh: String): Result<Unit, DomainError>
    suspend fun loginWithBiometrics(): Result<Boolean, DomainError>
}

