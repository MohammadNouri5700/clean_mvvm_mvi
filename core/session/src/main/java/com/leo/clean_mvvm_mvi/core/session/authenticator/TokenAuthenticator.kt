package com.leo.clean_mvvm_mvi.core.session.authenticator

import com.leo.clean_mvvm_mvi.core.session.token.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenStorage: TokenStorage
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            // Attempt token refresh synchronously
            runBlocking {
                tokenStorage.clearTokens()
            }
        }
        return null
    }
}
