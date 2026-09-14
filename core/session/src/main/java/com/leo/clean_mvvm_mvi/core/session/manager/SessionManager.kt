package com.leo.clean_mvvm_mvi.core.session.manager

import com.leo.clean_mvvm_mvi.core.session.token.TokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    val isLoggedIn: Flow<Boolean> = tokenStorage.accessToken.map { !it.isNull_or_empty() }

    suspend fun onSessionStarted(accessToken: String, refreshToken: String) {
        tokenStorage.saveTokens(accessToken, refreshToken)
    }

    suspend fun logout() {
        tokenStorage.clearTokens()
    }

    private fun String?.isNull_or_empty(): Boolean = this.isNullOrEmpty()
}
