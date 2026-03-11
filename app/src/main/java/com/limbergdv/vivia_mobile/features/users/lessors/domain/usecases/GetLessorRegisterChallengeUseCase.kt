package com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import javax.inject.Inject

class GetLessorRegisterChallengeUseCase @Inject constructor(
    private val repository: LessorRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        companyName: String
    ): Result<String> {
        return try {
            if (firstName.isBlank() || lastName.isBlank() || companyName.isBlank()) {
                return Result.failure(Exception("Todos los campos son obligatorios"))
            }
            repository.getRegistrationChallenge(firstName, lastName, companyName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}