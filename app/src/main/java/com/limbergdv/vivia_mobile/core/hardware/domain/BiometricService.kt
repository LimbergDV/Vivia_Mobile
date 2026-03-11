package com.limbergdv.vivia_mobile.core.hardware.domain

import android.content.Context

interface BiometricService {
    /**
     * Paso 1.5 del Registro: Muestra el diálogo de huella y crea la credencial.
     * @param challengeJson El JSON devuelto por el servidor en el paso 1.
     * @return Result con el credentialResponseJson para enviar al servidor.
     */
    suspend fun registerBiometric(context: Context, challengeJson: String): Result<String>

    /**
     * Paso 1.5 del Login: Muestra el diálogo de huella y verifica la credencial.
     * @param challengeJson El JSON devuelto por el servidor en el paso 1.
     * @return Result con el credentialResponseJson para enviar al servidor.
     */
    suspend fun authenticateBiometric(context: Context, challengeJson: String): Result<String>
}