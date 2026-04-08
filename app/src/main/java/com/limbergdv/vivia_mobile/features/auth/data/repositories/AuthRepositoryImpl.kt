package com.limbergdv.vivia_mobile.features.auth.data.repositories

import android.util.Log
import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.api.AuthApi
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.LoginRequestDto
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.RefreshTokenRequestDto
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.VerifyLoginRequestDto
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.mappers.toDomain
import com.limbergdv.vivia_mobile.features.auth.domain.entities.AuthToken
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore,
) : AuthRepository {

    override suspend fun loginTraditional(identifier: String, password: String): Result<AuthToken> {
        return try {
            val request = LoginRequestDto(identifier, password)
            val response = authApi.loginTraditional(request)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val authToken = body.data.toDomain()
                    tokenDataStore.saveTokens(authToken.accessToken, authToken.refreshToken)
                    
                    Log.d("VIVIA_AUTH_DEBUG", "🔑 Traditional Login Success")
                    Log.d("VIVIA_AUTH_DEBUG", "Token: ${authToken.accessToken}")
                    
                    Result.success(authToken)
                } else {
                    Result.failure(Exception(body?.message ?: "Error desconocido en el servidor"))
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

    override suspend fun getLoginChallenge(): Result<String> {
        return try {
            val response = authApi.getLoginChallenge()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al obtener el desafío"))
                }
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyLogin(credentialResponseJson: String): Result<AuthToken> {
        return try {
            val request = VerifyLoginRequestDto(credentialResponseJson)
            val response = authApi.verifyLogin(request)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val authToken = body.data.toDomain()
                    tokenDataStore.saveTokens(authToken.accessToken, authToken.refreshToken)
                    
                    Log.d("VIVIA_AUTH_DEBUG", "🔑 Biometric Login Success")
                    
                    Result.success(authToken)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al verificar la credencial"))
                }
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthToken> {
        return try {
            val request = RefreshTokenRequestDto(refreshToken)
            val response = authApi.refreshToken(request).execute()
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    val authToken = body.data.toDomain()
                    tokenDataStore.saveTokens(authToken.accessToken, authToken.refreshToken)
                    Result.success(authToken)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al refrescar el token"))
                }
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = authApi.logout()
            if (response.isSuccessful) {
                tokenDataStore.clearTokens()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al cerrar sesión (HTTP ${response.code()})"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
