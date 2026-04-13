package com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import javax.inject.Inject

class VerifyLessorRegistrationUseCase @Inject constructor(
    private val repository: LessorRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        companyName: String,
        password: String,
        phoneNumber: String,
        credentialResponseJson: String
    ): Result<Unit> {
        return try {
            repository.verifyRegistration(
                firstName = firstName,
                lastName = lastName,
                companyName = companyName,
                password = password,
                phoneNumber = phoneNumber,
                credentialResponseJson = credentialResponseJson
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
