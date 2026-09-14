package com.leo.clean_mvvm_mvi.core.database.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leo.clean_mvvm_mvi.core.network.model.Driver
import com.leo.clean_mvvm_mvi.core.network.model.OAuth
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"
val Context.appdataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

@Singleton
class AppDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val secureStorage: SecureStorage,
) {

    private val gson = Gson()

    companion object {
        // Session
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val DRIVER_KEY = stringPreferencesKey("driver_json")

        // Credentials
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")

        // UI
        private val THEME_KEY = stringPreferencesKey("theme")
        private val LANGUAGE_KEY = stringPreferencesKey("language")

        // Shift
        private val IS_IN_SHIFT_KEY = booleanPreferencesKey("is_in_shift")
        private val WS_URL_KEY = stringPreferencesKey("ws_url")
    }

    // ====================================================
    // 🔐 OAuth
    // ====================================================

    suspend fun saveOAuth(oAuth: OAuth) {
        context.appdataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = secureStorage.encrypt(oAuth.access)
            prefs[REFRESH_TOKEN] = secureStorage.encrypt(oAuth.refresh)
        }
    }

    suspend fun saveAccessToken(token: String) {
        context.appdataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = secureStorage.encrypt(token)
        }
    }

    val accessTokenFlow: Flow<String?> =
        context.appdataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                val encrypted = prefs[ACCESS_TOKEN]
                secureStorage.decrypt(encrypted)
            }

    suspend fun getAccessToken(): String? {
        val encrypted = context.appdataStore.data.first()[ACCESS_TOKEN]
        return secureStorage.decrypt(encrypted)
    }

    suspend fun getRefreshToken(): String? {
        val encrypted = context.appdataStore.data.first()[REFRESH_TOKEN]
        return secureStorage.decrypt(encrypted)
    }

    // ====================================================
    // 👤 Driver
    // ====================================================

    suspend fun saveDriver(driver: Driver) {
        val json = gson.toJson(driver)
        val encrypted = secureStorage.encrypt(json)

        context.appdataStore.edit { prefs ->
            prefs[DRIVER_KEY] = encrypted
        }
    }

    val driverFlow: Flow<Driver?> =
        context.appdataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                val encrypted = prefs[DRIVER_KEY]
                val json = secureStorage.decrypt(encrypted)
                json?.let { gson.fromJson(it, Driver::class.java) }
            }

    // ====================================================
    // 🔑 Credentials (Biometric support)
    // ====================================================

    suspend fun saveCredentials(username: String, password: String) {
        context.appdataStore.edit { prefs ->
            prefs[USERNAME_KEY] = secureStorage.encrypt(username)
            prefs[PASSWORD_KEY] = secureStorage.encrypt(password)
        }
    }

    val credentialsFlow: Flow<Pair<String?, String?>> =
        context.appdataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                val username = secureStorage.decrypt(prefs[USERNAME_KEY])
                val password = secureStorage.decrypt(prefs[PASSWORD_KEY])
                username to password
            }

    suspend fun clearCredentials() {
        context.appdataStore.edit { prefs ->
            prefs.remove(USERNAME_KEY)
            prefs.remove(PASSWORD_KEY)
        }
    }

    // ====================================================
    // 🎨 Theme
    // ====================================================

    suspend fun saveThemeToPreferences(theme: AppTheme) {
        context.appdataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }

    val themePreferenceFlow: Flow<AppTheme> =
        context.appdataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                AppTheme.valueOf(
                    prefs[THEME_KEY] ?: AppTheme.SYSTEM.name
                )
            }

    // ====================================================
    // 🌍 Language
    // ====================================================

    suspend fun saveLanguageToPreferences(lang: String) {
        context.appdataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }

    val languagePreferenceFlow: Flow<String> =
        context.appdataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                prefs[LANGUAGE_KEY] ?: "fa"
            }

    // ====================================================
    // 🚚 Shift
    // ====================================================

    suspend fun saveIsInShift(isInShift: Boolean) {
        context.appdataStore.edit { prefs ->
            prefs[IS_IN_SHIFT_KEY] = isInShift
        }
    }

    val isInShiftFlow: Flow<Boolean> =
        context.appdataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                prefs[IS_IN_SHIFT_KEY] ?: false
            }

    // ====================================================
    // 🌐 WebSocket
    // ====================================================

    suspend fun saveWsUrl(url: String) {
        context.appdataStore.edit { prefs ->
            prefs[WS_URL_KEY] = url
        }
    }

    val wsUrlFlow: Flow<String?> =
        context.appdataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                prefs[WS_URL_KEY]
            }

    // ====================================================
    // 🚪 Logout / Clear Session
    // ====================================================

    suspend fun clearSession() {
        context.appdataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
            prefs.remove(DRIVER_KEY)
            prefs.remove(IS_IN_SHIFT_KEY)
        }
    }

    suspend fun clearAll() {
        context.appdataStore.edit { it.clear() }
    }
}

