package com.leo.clean_mvvm_mvi.feature.auth.domain.model

data class UserAuth(
    val accessToken: String = "",
    val refreshToken: String = "",
    val driverId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val avatar: String = "",
    val username: String = "",
    val isLoggedIn: Boolean = false,
)

