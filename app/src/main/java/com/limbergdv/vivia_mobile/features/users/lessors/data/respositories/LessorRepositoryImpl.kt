package com.limbergdv.vivia_mobile.features.users.lessors.data.respositories

import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.api.LessorApi
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorRegisterChallengeRequestDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorRegisterVerifyRequestDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.mappers.toDomain
import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor
import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import javax.inject.Inject

class LessorRepositoryImpl @Inject constructor(
    private val api: LessorApi,
) : LessorRepository {

    override suspend fun getRegistrationChallenge(
        firstName: String,
        lastName: String,
        companyName: String
    ): Result<String> {
        return try {
            // 1. Armamos el Request DTO
            val request = LessorRegisterChallengeRequestDto(
                firstName = firstName,
                lastName = lastName,
                companyName = companyName
            )

            // 2. Ejecutamos la llamada a la API
            val response = api.lessorRegisterChallenge(request)

            // 3. Evaluamos la respuesta base
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al obtener el desafío biométrico del servidor"))
            }
        } catch (e: Exception) {
            // Capturamos problemas de red (IOException) o de servidor (HttpException)
            Result.failure(e)
        }
    }

    override suspend fun verifyRegistration(
        companyName: String,
        credentialResponseJson: String
    ): Result<Lessor> {
        return try {
            // 1. Armamos el Request DTO
            val request = LessorRegisterVerifyRequestDto(
                companyName = companyName,
                credentialResponseJson = credentialResponseJson
            )

            // 2. Ejecutamos la llamada a la API
            val response = api.lessorRegisterVerify(request)

            // 3. Evaluamos la respuesta
            if (response.success && response.data != null) {
                // 4. Mapeamos el DTO a la Entidad de Dominio
                val lessor = response.data.toDomain()
                Result.success(lessor)
            } else {
                Result.failure(Exception(response.message ?: "Error al verificar y guardar el registro del arrendador"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}