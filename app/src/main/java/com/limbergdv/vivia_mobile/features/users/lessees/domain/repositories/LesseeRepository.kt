package com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories

interface LesseeRepository {

    suspend fun getRegistrationChallenge(
        username: String,
        email: String,
        password: String
    ): Result<String>

    suspend fun verifyRegistration(
        email: String,
        credentialResponseJson: String
    ): Result<Unit>

    suspend fun updateFcmToken(token: String): Result<String>

    suspend fun followLessor(companyName: String): Result<String>
}
