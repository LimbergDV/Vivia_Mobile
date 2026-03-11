package com.limbergdv.vivia_mobile.features.users.lessees.data.repositories

import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.api.LesseeApi
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LesseeRegisterChallengeRequestDto
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LesseeRegisterVerifyRequestDto
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.mappers.toDomain
import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.Lessee
import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class LesseeRepositoryImpl @Inject constructor(
    private val api: LesseeApi
) : LesseeRepository {

    override suspend fun getRegistrationChallenge(
        username: String,
        email: String
    ): Result<String> {
        return try {
            val request = LesseeRegisterChallengeRequestDto(
                username = username,
                email = email
            )
            val response = api.lesseeRegisterChallenge(request)

            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al obtener el desafío biométrico"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyRegistration(
        email: String,
        credentialResponseJson: String
    ): Result<Lessee> {
        return try {
            val request = LesseeRegisterVerifyRequestDto(
                email = email,
                credentialResponseJson = credentialResponseJson
            )
            val response = api.lesseeRegisterVerify(request)

            if (response.success && response.data != null) {
                Result.success(response.data.toDomain())
            } else {
                Result.failure(Exception(response.message ?: "Error al verificar el registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}