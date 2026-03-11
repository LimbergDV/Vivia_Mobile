package com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.Lessee
import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class VerifyLesseeRegistrationUseCase @Inject constructor(
    private val repository: LesseeRepository
) {

    suspend operator fun invoke(
        email: String,
        credentialResponseJson: String
    ) : Result<Lessee> {
        return try {
            if (email.isBlank() || credentialResponseJson.isBlank()) {
                return Result.failure(Exception("Faltan datos de validación biométrica"))
            }
            repository.verifyRegistration(email, credentialResponseJson)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}