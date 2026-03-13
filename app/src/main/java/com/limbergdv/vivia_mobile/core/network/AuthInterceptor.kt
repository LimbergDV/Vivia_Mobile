package com.limbergdv.vivia_mobile.core.network

import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor @Inject constructor (
    private val tokenDataStore: TokenDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenDataStore.getToken()

        // Si no hay token, hacemos la petición normal (ej. Login/Register)
        if (token == null) {
            return chain.proceed(originalRequest)
        }

        // Si hay token, creamos una nueva petición con el header
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}