package com.leo.clean_mvvm_mvi.core.session.pkce

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuthPkceAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun launchAuthFlow(authUrl: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, Uri.parse(authUrl))
    }
}
