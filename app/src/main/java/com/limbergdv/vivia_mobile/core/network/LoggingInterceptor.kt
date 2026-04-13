package com.limbergdv.vivia_mobile.core.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log de la petición
        Log.d("HTTP", "╔════════════════════════════════════════════════════")
        Log.d("HTTP", "║ ${request.method} ${request.url}")
        Log.d("HTTP", "╟────────────────────────────────────────────────────")

        // Headers
        request.headers.forEach { (name, value) ->
            if (name.lowercase() != "authorization") { // No mostrar token completo
                Log.d("HTTP", "║ Header: $name = $value")
            } else {
                Log.d("HTTP", "║ Header: $name = Bearer ***")
            }
        }

        // Body
        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = Charset.forName("UTF-8")
            val content = buffer.readString(charset)

            Log.d("HTTP", "╟────────────────────────────────────────────────────")
            Log.d("HTTP", "║ REQUEST BODY:")
            content.lines().forEach { line ->
                Log.d("HTTP", "║ $line")
            }
        }

        Log.d("HTTP", "╚════════════════════════════════════════════════════")

        // Ejecutar la petición
        val response = chain.proceed(request)

        // Log de la respuesta
        Log.d("HTTP", "╔════════════════════════════════════════════════════")
        Log.d("HTTP", "║ RESPONSE: ${response.code} ${response.message}")
        Log.d("HTTP", "╚════════════════════════════════════════════════════")

        return response
    }
}
