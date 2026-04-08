package com.limbergdv.vivia_mobile.features.auth.domain.repositories

import com.limbergdv.vivia_mobile.features.auth.domain.entities.AuthToken

interface AuthRepository {
    suspend fun loginTraditional(identifier: String, password: String): Result<AuthToken>
    suspend fun getLoginChallenge(): Result<String>
    suspend fun verifyLogin(credentialResponseJson: String): Result<AuthToken>
    suspend fun refreshToken(refreshToken: String): Result<AuthToken>
    suspend fun logout(): Result<Unit>
}
