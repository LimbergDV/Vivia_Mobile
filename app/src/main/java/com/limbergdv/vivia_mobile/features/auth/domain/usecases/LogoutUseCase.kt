package com.limbergdv.vivia_mobile.features.auth.domain.usecases

import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) {
    suspend operator fun invoke(): Result<Unit> {
        // Siempre limpiamos los tokens locales primero
        // Esto asegura que el usuario pueda cerrar sesión incluso si el backend falla
        return try {
            val result = repository.logout()
            // Limpiamos tokens sin importar el resultado del servidor
            tokenDataStore.clearTokens()

            result.fold(
                onSuccess = { Result.success(Unit) },
                onFailure = {
                    // Aunque el servidor falle, consideramos el logout exitoso
                    // porque los tokens locales ya fueron limpiados
                    android.util.Log.w("LogoutUseCase", "Server logout failed but local tokens cleared: ${it.message}")
                    Result.success(Unit)
                }
            )
        } catch (e: Exception) {
            // Garantizamos que los tokens se limpien incluso si hay una excepción
            tokenDataStore.clearTokens()
            Result.success(Unit)
        }
    }
}
