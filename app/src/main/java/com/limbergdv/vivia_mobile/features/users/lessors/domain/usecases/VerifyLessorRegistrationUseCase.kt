package com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor
import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import javax.inject.Inject

class VerifyLessorRegistrationUseCase @Inject constructor(
    private val repository: LessorRepository
) {
    suspend operator fun invoke(
        companyName: String,
        credentialResponseJson: String
    ): Result<Lessor> {
        return try {
            if (companyName.isBlank() || credentialResponseJson.isBlank()) {
                return Result.failure(Exception("Faltan datos de validación biométrica"))
            }
            repository.verifyRegistration(companyName, credentialResponseJson)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}