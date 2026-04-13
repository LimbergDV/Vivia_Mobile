package com.limbergdv.vivia_mobile.core.session

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDataStore @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_TYPE = "user_type"
    }

    enum class UserType {
        LESSEE, LESSOR
    }

    val accessTokenFlow: Flow<String?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == ACCESS_TOKEN) {
                trySend(sharedPreferences.getString(ACCESS_TOKEN, null))
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(prefs.getString(ACCESS_TOKEN, null))
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String, userType: UserType? = null) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.putString(REFRESH_TOKEN, refreshToken)
        userType?.let { editor.putString(USER_TYPE, it.name) }
        editor.apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun getUserType(): UserType? {
        return prefs.getString(USER_TYPE, null)?.let {
            try {
                UserType.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    suspend fun clearTokens() {
        prefs.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .remove(USER_TYPE)
            .apply()
    }
}
