package com.limbergdv.vivia_mobile.core.network

import android.util.Log
import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.api.AuthApi
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.RefreshTokenRequestDto
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.mappers.toDomain
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val authApi: Lazy<AuthApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
        // Evitar bucles infinitos de reintentos
        if (response.priorResponse != null && response.priorResponse?.code == 401) {
            return null
        }

        synchronized(this) {
            Log.d("VIVIA_AUTH_DEBUG", "Intentando renovar token...")
            
            val refreshToken = tokenDataStore.getRefreshToken() ?: return null

            // Llamada síncrona para refrescar el token
            // Nota: authApi.get() devuelve la instancia de AuthApi
            val refreshResponse = authApi.get().refreshToken(RefreshTokenRequestDto(refreshToken)).execute()

            if (refreshResponse.isSuccessful) {
                val body = refreshResponse.body()
                if (body?.success == true && body.data != null) {
                    val newTokens = body.data.toDomain()
                    
                    runBlocking {
                        tokenDataStore.saveTokens(newTokens.accessToken, newTokens.refreshToken)
                    }

                    Log.d("VIVIA_AUTH_DEBUG", "Token renovado exitosamente")

                    return response.request.newBuilder()
                        .header("Authorization", "Bearer ${newTokens.accessToken}")
                        .build()
                }
            }

            // Si el refresco falla (ej. refresh token expirado)
            Log.e("VIVIA_AUTH_DEBUG", "Fallo al renovar token, cerrando sesión")
            runBlocking {
                tokenDataStore.clearTokens()
            }
            return null
        }
    }
}
