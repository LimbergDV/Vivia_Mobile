package com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases

import android.content.Context
import android.util.Log
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
        Log.d("RegisterLessorUC", "🔹 PASO 1: Solicitando challenge con datos:")
        Log.d("RegisterLessorUC", "  firstName: '$firstName'")
        Log.d("RegisterLessorUC", "  lastName: '$lastName'")
        Log.d("RegisterLessorUC", "  companyName: '$companyName'")
        Log.d("RegisterLessorUC", "  password: '$password'")
        Log.d("RegisterLessorUC", "  phoneNumber: '$phoneNumber' (length: ${phoneNumber.length})")

        // 1. Obtener desafío del servidor
        val challengeResult = repository.getRegistrationChallenge(
            firstName, lastName, companyName, password, phoneNumber
        )
        val challengeJson = challengeResult.getOrElse {
            Log.e("RegisterLessorUC", "❌ Error obteniendo challenge: ${it.message}")
            return Result.failure(it)
        }

        Log.d("RegisterLessorUC", "✅ Challenge obtenido exitosamente")

        // 2. Ejecutar biometría en el hardware
        Log.d("RegisterLessorUC", "🔹 PASO 2: Ejecutando biometría")
        val biometricResult = biometricService.registerBiometric(context, challengeJson)
        val credentialResponseJson = biometricResult.getOrElse {
            Log.e("RegisterLessorUC", "❌ Error en biometría: ${it.message}")
            return Result.failure(it)
        }

        Log.d("RegisterLessorUC", "✅ Biometría completada")

        // 3. Verificar en el servidor
        Log.d("RegisterLessorUC", "🔹 PASO 3: Verificando registro con:")
        Log.d("RegisterLessorUC", "  companyName: '$companyName'")
        Log.d("RegisterLessorUC", "  credentialResponseJson: ${credentialResponseJson.take(50)}...")

        return repository.verifyRegistration(
            companyName = companyName,
            credentialResponseJson = credentialResponseJson
        )
    }
}
