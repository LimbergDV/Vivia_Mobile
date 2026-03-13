package com.limbergdv.vivia_mobile.features.auth.domain.repositories

import com.limbergdv.vivia_mobile.features.auth.domain.entities.AuthToken

interface AuthRepository {

    // Paso 1: Solicitar desafío de registro
    suspend fun getRegistrationChallenge(): Result<String>

    // Paso 2: Verificar credencial
    suspend fun verifyRegistration( credentialResponseJson: String): Result<AuthToken>
}