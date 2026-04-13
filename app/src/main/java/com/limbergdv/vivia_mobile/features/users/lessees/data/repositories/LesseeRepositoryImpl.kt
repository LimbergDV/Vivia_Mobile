package com.limbergdv.vivia_mobile.features.users.lessees.data.repositories

import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.api.LesseeApi
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.RegisterLesseeChallengeDto
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.VerifyLesseeRegistrationDto
import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LesseeRepositoryImpl @Inject constructor(
    private val api: LesseeApi
) : LesseeRepository {

    override suspend fun getRegistrationChallenge(
        username: String,
        email: String,
        password: String
    ): Result<String> {
        return try {
            val request = RegisterLesseeChallengeDto(
                username = username,
                email = email,
                password = password
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
        email: String,
        credentialResponseJson: String
    ): Result<Unit> {
        return try {
            val request = VerifyLesseeRegistrationDto(
                email = email,
                credentialResponseJson = credentialResponseJson
            )
            val response = api.verifyRegistration(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al verificar el registro"))
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

    override suspend fun updateFcmToken(token: String): Result<String> {
        return try {
            val response = api.updateFcmToken(token)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(body.data ?: "Token FCM actualizado correctamente")
                } else {
                    Result.failure(Exception(body?.message ?: "Error al actualizar token FCM"))
                }
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun followLessor(companyName: String): Result<String> {
        return try {
            val response = api.followLessor(companyName)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(body.data ?: "Arrendador seguido con éxito")
                } else {
                    Result.failure(Exception(body?.message ?: "Error al seguir al arrendador"))
                }
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
