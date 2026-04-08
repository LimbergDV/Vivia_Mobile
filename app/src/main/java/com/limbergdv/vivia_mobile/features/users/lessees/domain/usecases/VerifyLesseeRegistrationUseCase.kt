package com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class VerifyLesseeRegistrationUseCase @Inject constructor(
    private val repository: LesseeRepository
) {
    suspend operator fun invoke(
        credentialResponseJson: String
    ): Result<Unit> {
        return try {
            repository.verifyRegistration(credentialResponseJson)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
