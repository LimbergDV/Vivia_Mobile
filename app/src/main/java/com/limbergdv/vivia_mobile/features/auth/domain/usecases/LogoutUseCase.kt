package com.limbergdv.vivia_mobile.features.auth.domain.usecases

import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout().fold(
            onSuccess = {
                // Los tokens ya se limpian en el repositorio, pero por si acaso
                tokenDataStore.clearTokens()
                Result.success(Unit)
            },
            onFailure = { error ->
                // Incluso si el API falla, limpiamos los tokens locales
                tokenDataStore.clearTokens()
                Result.failure(error)
            }
        )
    }
}
