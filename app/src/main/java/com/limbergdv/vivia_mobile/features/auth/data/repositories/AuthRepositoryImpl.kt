package com.limbergdv.vivia_mobile.features.auth.data.repositories

import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.api.AuthApi
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.AuthVerifyRequestDto
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.mappers.toDomain
import com.limbergdv.vivia_mobile.features.auth.domain.entities.AuthToken
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore,
) : AuthRepository {

    override suspend fun getRegistrationChallenge(): Result<String> {
        return try {

            val response = authApi.authChallenge()

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

    override suspend fun verifyRegistration(credentialResponseJson: String): Result<AuthToken> {
        return try {
            // 1. Armamos el Request DTO
            val request = AuthVerifyRequestDto (
                credentialResponseJson = credentialResponseJson
            )

            // 2. Ejecutamos la llamada a la API
            val response = authApi.authVerify(request)

            // 3. Evaluamos la respuesta
            if (response.success && response.data != null) {
                // 4. Mapeamos el DTO a la Entidad de Dominio
                val authToken = response.data.toDomain()

                tokenDataStore.saveToken(authToken.token)

                Result.success(authToken)
            } else {
                Result.failure(Exception(response.message ?: "Error al iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}