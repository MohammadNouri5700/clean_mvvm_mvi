package com.leo.clean_mvvm_mvi.feature.auth.domain.usecase

import com.leo.clean_mvvm_mvi.core.domain.result.DomainError
import com.leo.clean_mvvm_mvi.core.domain.result.Result
import com.leo.clean_mvvm_mvi.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String): Result<Boolean, DomainError> {
        return authRepository.sendOtp(phoneNumber)
    }
}

