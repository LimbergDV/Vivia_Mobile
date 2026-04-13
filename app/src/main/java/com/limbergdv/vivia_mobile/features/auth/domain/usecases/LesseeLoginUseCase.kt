package com.limbergdv.vivia_mobile.features.auth.domain.usecases

import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LesseeLoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) {
    suspend operator fun invoke(identifier: String, password: String): Result<Unit> {
        return repository.loginTraditional(identifier, password).fold(
            onSuccess = { authToken ->
                tokenDataStore.saveTokens(
                    accessToken = authToken.accessToken,
                    refreshToken = authToken.refreshToken,
                    userType = TokenDataStore.UserType.LESSEE
                )
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}
