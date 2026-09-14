package com.leo.clean_mvvm_mvi.feature.auth.data.mapper

import com.leo.clean_mvvm_mvi.core.network.model.Driver
import com.leo.clean_mvvm_mvi.core.network.model.OAuth
import com.leo.clean_mvvm_mvi.core.network.model.RefreshTokenData
import com.leo.clean_mvvm_mvi.core.network.model.VerifyOtpData
import com.leo.clean_mvvm_mvi.feature.auth.domain.model.UserAuth

fun VerifyOtpData.toDomain(): UserAuth {
    return UserAuth(
        accessToken = access,
        refreshToken = refresh,
        driverId = driver.driverId.orEmpty(),
        firstName = driver.firstName.orEmpty(),
        lastName = driver.lastName.orEmpty(),
        phoneNumber = driver.phoneNumber.orEmpty(),
        avatar = driver.avatar.orEmpty(),
        username = driver.username.orEmpty(),
        isLoggedIn = true,
    )
}

fun Driver.toDomain(): UserAuth {
    return UserAuth(
        driverId = driverId.orEmpty(),
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        phoneNumber = phoneNumber.orEmpty(),
        avatar = avatar.orEmpty(),
        username = username.orEmpty(),
        isLoggedIn = true,
    )
}

fun OAuth.toDomain(): UserAuth {
    return UserAuth(
        accessToken = access.orEmpty(),
        refreshToken = refresh.orEmpty(),
        isLoggedIn = true,
    )
}

fun RefreshTokenData.toDomainToken(): String {
    return access
}

