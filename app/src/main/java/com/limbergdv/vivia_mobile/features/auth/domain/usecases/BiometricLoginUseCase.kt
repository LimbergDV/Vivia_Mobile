package com.limbergdv.vivia_mobile.features.auth.domain.usecases

import android.content.Context
import com.limbergdv.vivia_mobile.core.hardware.domain.BiometricService
import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class BiometricLoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val biometricService: BiometricService,
    private val tokenDataStore: TokenDataStore
) {
    suspend operator fun invoke(context: Context, userType: TokenDataStore.UserType): Result<Unit> {
        // 1. Obtener desafío del servidor
        val challengeResult = repository.getLoginChallenge()
        val challengeJson = challengeResult.getOrElse { return Result.failure(it) }

        // 2. Ejecutar biometría en el hardware
        val biometricResult = biometricService.authenticateBiometric(context, challengeJson)
        val credentialResponseJson = biometricResult.getOrElse { return Result.failure(it) }

        // 3. Verificar en el servidor
        val verifyResult = repository.verifyLogin(credentialResponseJson)

        return verifyResult.fold(
            onSuccess = { authToken ->
                // 4. Guardar tokens con el tipo de usuario
                tokenDataStore.saveTokens(
                    accessToken = authToken.accessToken,
                    refreshToken = authToken.refreshToken,
                    userType = userType
                )
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}
