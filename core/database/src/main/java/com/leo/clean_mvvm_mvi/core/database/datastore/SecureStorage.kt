package com.leo.clean_mvvm_mvi.core.database.datastore

import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    companion object {
        private const val KEY_PREF = "aes_key"
        private const val PREF_NAME = "secure_key_store"
    }

    private val key: ByteArray by lazy {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedKey = prefs.getString(KEY_PREF, null)

        if (savedKey != null) {
            Base64.decode(savedKey, Base64.DEFAULT)
        } else {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(256)
            val newKey = keyGen.generateKey().encoded
            prefs.edit {
                putString(KEY_PREF, Base64.encodeToString(newKey, Base64.DEFAULT))
            }
            newKey
        }
    }

    fun encrypt(value: String?): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12).apply { SecureRandom().nextBytes(this) }
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(key, "AES"),
            GCMParameterSpec(128, iv)
        )
        val encrypted = cipher.doFinal(value?.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    fun decrypt(value: String?): String? {
        return try {
            val bytes = Base64.decode(value, Base64.DEFAULT)
            val iv = bytes.copyOfRange(0, 12)
            val encrypted = bytes.copyOfRange(12, bytes.size)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(key, "AES"),
                GCMParameterSpec(128, iv)
            )
            String(cipher.doFinal(encrypted))
        } catch (e: Exception) {
            null
        }
    }
}

