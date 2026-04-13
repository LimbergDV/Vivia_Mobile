package com.limbergdv.vivia_mobile.features.users.lessors.data.repositories

import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.api.LessorApi
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.RegisterLessorChallengeDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.VerifyLessorRegistrationDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.mappers.toDomain
import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor
import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LessorRepositoryImpl @Inject constructor(
    private val api: LessorApi,
) : LessorRepository {

    override suspend fun getRegistrationChallenge(
        firstName: String,
        lastName: String,
        companyName: String,
        password: String,
        phoneNumber: String
    ): Result<String> {
        return try {
            val request = RegisterLessorChallengeDto(
                firstName = firstName,
                lastName = lastName,
                companyName = companyName,
                password = password,
                phoneNumber = phoneNumber
            )
            val response = api.getRegistrationChallenge(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener el desafío biométrico"))
                }
            } else {
                Result.failure(Exception("Error en la conexión con el servidor (HTTP ${response.code()})"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión a internet", e))
        } catch (e: HttpException) {
            Result.failure(Exception("Error en el servidor", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyRegistration(
        firstName: String,
        lastName: String,
        companyName: String,
        password: String,
        phoneNumber: String,
        credentialResponseJson: String
    ): Result<Unit> {
        return try {
            val request = VerifyLessorRegistrationDto(
                firstName = firstName,
                lastName = lastName,
                companyName = companyName,
                password = password,
                phoneNumber = phoneNumber,
                credentialResponseJson = credentialResponseJson
            )
            val response = api.verifyRegistration(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al verificar el registro del arrendador"))
                }
            } else {
                Result.failure(Exception("Error en la conexión con el servidor (HTTP ${response.code()})"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión a internet", e))
        } catch (e: HttpException) {
            Result.failure(Exception("Error en el servidor", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllLessors(): Result<List<Lessor>> {
        return try {
            val response = api.getAllLessors()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.success(body.data.map { it.toDomain() })
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener la lista de arrendadores"))
                }
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
