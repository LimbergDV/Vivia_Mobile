package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.GetAuthChallengeUseCase
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.VerifyAuthVerifyUseCase
import com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases.UpdateFcmTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginLesseeViewModel @Inject constructor(
    private val getChallengeUseCase: GetAuthChallengeUseCase,
    private val verifyUseCase: VerifyAuthVerifyUseCase,
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginLesseeState())
    val state: StateFlow<LoginLesseeState> = _state.asStateFlow()

    fun onEvent(event: LoginLesseeEvent) {
        when (event) {
            is LoginLesseeEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is LoginLesseeEvent.LoginClicked -> getChallenge()
            is LoginLesseeEvent.OnBiometricSuccess -> verifyLogin(event.credentialResponseJson)
            is LoginLesseeEvent.OnBiometricError -> _state.update { it.copy(isLoading = false, error = event.error) }
            is LoginLesseeEvent.ConsumeChallenge -> _state.update { it.copy(webAuthnChallenge = null) }
            is LoginLesseeEvent.ConsumeError -> _state.update { it.copy(error = null) }
        }
    }

    private fun getChallenge() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // 1. Validación del correo
            if (!_state.value.email.contains("@")) {
                _state.update { it.copy(isLoading = false, error = "El correo electrónico no es válido") }
                return@launch // IMPORTANTE: Detiene la ejecución aquí si hay error
            }

            val result = getChallengeUseCase()
            result.fold(
                onSuccess = { challengeJson ->
                    _state.update { it.copy(isLoading = false, webAuthnChallenge = challengeJson) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message ?: "Error al obtener desafío") }
                }
            )
        }
    }

    private fun verifyLogin(credentialResponseJson: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = verifyUseCase(credentialResponseJson)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                    // Disparamos la sincronización del token justo al tener éxito
                    syncFirebaseToken()
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message ?: "Error al iniciar sesión") }
                }
            )
        }
    }

    private fun syncFirebaseToken() {
        Log.d("VIVIA_FCM_DEBUG", "Iniciando petición de token a Firebase...")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(
                    "VIVIA_FCM_DEBUG",
                    "Fallo al obtener el token FCM: ${task.exception?.message}"
                )
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("VIVIA_FCM_DEBUG", "====================================")
            Log.d("VIVIA_FCM_DEBUG", "📱 FCM TOKEN GENERADO EXITOSAMENTE")
            Log.d("VIVIA_FCM_DEBUG", token)
            Log.d("VIVIA_FCM_DEBUG", "====================================")

            viewModelScope.launch {
                Log.d("VIVIA_FCM_DEBUG", "Enviando token al backend...")
                val result = updateFcmTokenUseCase(token)

                // Asumiendo que tu useCase devuelve un Result
                result.fold(
                    onSuccess = {
                        Log.d("VIVIA_FCM_DEBUG", "✅ Token guardado en el backend con éxito")
                    },
                    onFailure = { e ->
                        Log.e(
                            "VIVIA_FCM_DEBUG",
                            "❌ Error al guardar token en backend: ${e.message}"
                        )
                    }
                )
            }
        }
    }

}