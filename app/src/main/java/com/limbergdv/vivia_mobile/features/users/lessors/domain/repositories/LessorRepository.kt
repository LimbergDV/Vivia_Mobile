package com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories

import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor

interface LessorRepository {

    // Paso 1: Solicitar desafío de registro
    suspend fun getRegistrationChallenge(
        firstName: String,
        lastName: String,
        companyName: String
    ): Result<String>

    // Paso 2: Verificar credencial y guardar arrendador
    suspend fun verifyRegistration(
        companyName: String,
        credentialResponseJson: String
    ): Result<Lessor>
}