package com.limbergdv.vivia_mobile.features.auth.domain.usecases

import com.limbergdv.vivia_mobile.features.auth.domain.entities.AuthToken
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class VerifyAuthVerifyUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke( credentialResponseJson: String) : Result<AuthToken> {
        return try {
            repository.verifyRegistration(credentialResponseJson)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}