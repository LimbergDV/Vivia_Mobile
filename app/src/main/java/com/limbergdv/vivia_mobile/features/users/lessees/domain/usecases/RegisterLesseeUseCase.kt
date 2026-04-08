package com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases

import android.content.Context
import com.limbergdv.vivia_mobile.core.hardware.domain.BiometricService
import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class RegisterLesseeUseCase @Inject constructor(
    private val repository: LesseeRepository,
    private val biometricService: BiometricService
) {
    suspend operator fun invoke(
        context: Context,
        username: String,
        email: String,
        password: String
    ): Result<Unit> {
        // 1. Obtener desafío del servidor
        val challengeResult = repository.getRegistrationChallenge(username, email, password)
        val challengeJson = challengeResult.getOrElse { return Result.failure(it) }

        // 2. Ejecutar biometría en el hardware
        val biometricResult = biometricService.registerBiometric(context, challengeJson)
        val credentialResponseJson = biometricResult.getOrElse { return Result.failure(it) }

        // 3. Verificar en el servidor
        return repository.verifyRegistration(credentialResponseJson)
    }
}
