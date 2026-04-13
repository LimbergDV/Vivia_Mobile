package com.limbergdv.vivia_mobile.features.auth.domain.usecases

import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class TraditionalLoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) {
    suspend operator fun invoke(identifier: String, password: String): Result<Unit> {
        return repository.loginTraditional(identifier, password).fold(
            onSuccess = { authToken ->
                tokenDataStore.saveTokens(authToken.accessToken, authToken.refreshToken)
                Result.success(Unit)
            },
            onFailure = { 
                Result.failure(it)
            }
        )
    }
}
