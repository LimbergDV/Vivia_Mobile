package com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories

import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.Lessee

interface LesseeRepository {

    suspend fun getRegistrationChallenge(
        username: String,
        email: String
    ): Result<String>

    suspend fun verifyRegistration(
        email: String,
        credentialResponseJson: String
    ): Result<Lessee>

    suspend fun updateFcmToken(token: String): Result<String>
}