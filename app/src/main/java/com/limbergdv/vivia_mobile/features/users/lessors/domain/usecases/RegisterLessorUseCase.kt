package com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases

import android.content.Context
import com.limbergdv.vivia_mobile.core.hardware.domain.BiometricService
import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import javax.inject.Inject

class RegisterLessorUseCase @Inject constructor(
    private val repository: LessorRepository,
    private val biometricService: BiometricService
) {
    suspend operator fun invoke(
        context: Context,
        firstName: String,
        lastName: String,
        companyName: String,
        password: String,
        phoneNumber: String
    ): Result<Unit> {
        // 1. Obtener desafío del servidor
        val challengeResult = repository.getRegistrationChallenge(
            firstName, lastName, companyName, password, phoneNumber
        )
        val challengeJson = challengeResult.getOrElse { return Result.failure(it) }

        // 2. Ejecutar biometría en el hardware
        val biometricResult = biometricService.registerBiometric(context, challengeJson)
        val credentialResponseJson = biometricResult.getOrElse { return Result.failure(it) }

        // 3. Verificar en el servidor
        return repository.verifyRegistration(credentialResponseJson)
    }
}
