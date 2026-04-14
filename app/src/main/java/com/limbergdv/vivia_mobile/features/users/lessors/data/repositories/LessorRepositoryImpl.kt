package com.limbergdv.vivia_mobile.features.users.lessors.data.repositories

import android.util.Log
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

            Log.d("LessorRepo", "📤 REQUEST DTO CREADO:")
            Log.d("LessorRepo", "  request.firstName = '${request.firstName}'")
            Log.d("LessorRepo", "  request.lastName = '${request.lastName}'")
            Log.d("LessorRepo", "  request.companyName = '${request.companyName}'")
            Log.d("LessorRepo", "  request.password = '${request.password}'")
            Log.d("LessorRepo", "  request.phoneNumber = '${request.phoneNumber}' (length: ${request.phoneNumber.length})")
            Log.d("LessorRepo", "📤 Enviando petición a /lessors/register/challenge...")

            val response = api.getRegistrationChallenge(request)

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("LessorRepo", "📥 RESPUESTA RECIBIDA (HTTP ${response.code()})")
                Log.d("LessorRepo", "  success: ${body?.success}")
                Log.d("LessorRepo", "  message: ${body?.message}")
                Log.d("LessorRepo", "  data exists: ${body?.data != null}")

                if (body?.success == true && body.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener el desafío biométrico"))
                }
            } else {
                Log.e("LessorRepo", "❌ Error HTTP ${response.code()}")
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
        companyName: String,
        credentialResponseJson: String
    ): Result<Unit> {
        return try {
            val request = VerifyLessorRegistrationDto(
                companyName = companyName,
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

    override suspend fun getMe(): Result<Lessor> {
        return try {
            val response = api.getMe()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.success(body.data.toDomain())
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener el perfil"))
                }
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
