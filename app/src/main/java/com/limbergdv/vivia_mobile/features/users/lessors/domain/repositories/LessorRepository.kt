package com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories

import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor

interface LessorRepository {

    suspend fun getRegistrationChallenge(
        firstName: String,
        lastName: String,
        companyName: String,
        password: String,
        phoneNumber: String
    ): Result<String>

    suspend fun verifyRegistration(
        firstName: String,
        lastName: String,
        companyName: String,
        password: String,
        phoneNumber: String,
        credentialResponseJson: String
    ): Result<Unit>

    suspend fun getAllLessors(): Result<List<Lessor>>
}
