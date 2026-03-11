package com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class GetLesseeRegisterChallengeUseCase @Inject constructor(
    private val repository: LesseeRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String
    ): Result<String> {
        return try {
            if (username.isBlank() || email.isBlank()) {
                return Result.failure(Exception("Todos los campos son obligatorios"))
            }

            repository.getRegistrationChallenge(username, email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}