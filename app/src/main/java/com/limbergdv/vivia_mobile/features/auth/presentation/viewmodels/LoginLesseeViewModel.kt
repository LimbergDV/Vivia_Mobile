package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.BiometricLoginUseCase
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.TraditionalLoginUseCase
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
    private val traditionalLoginUseCase: TraditionalLoginUseCase,
    private val biometricLoginUseCase: BiometricLoginUseCase,
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginLesseeState())
    val state: StateFlow<LoginLesseeState> = _state.asStateFlow()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginLesseeEvent) {
        when (event) {
            is LoginLesseeEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is LoginLesseeEvent.PasswordChanged -> _state.update { it.copy(password = event.password) }
            is LoginLesseeEvent.TraditionalLoginClicked -> loginTraditional()
            is LoginLesseeEvent.BiometricLoginClicked -> { /* This should call onBiometricLogin(context) from UI */ }
            is LoginLesseeEvent.ResetUiState -> _uiState.value = AuthUiState.Idle
        }
    }

    private fun loginTraditional() {
        val email = _state.value.email
        val password = _state.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Email y contraseña son requeridos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            traditionalLoginUseCase(email, password).fold(
                onSuccess = {
                    _uiState.value = AuthUiState.Success
                    syncFirebaseToken()
                },
                onFailure = { 
                    _uiState.value = AuthUiState.Error(it.message ?: "Error al iniciar sesión")
                }
            )
        }
    }

    fun onBiometricLogin(context: Context) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            biometricLoginUseCase(context).fold(
                onSuccess = {
                    _uiState.value = AuthUiState.Success
                    syncFirebaseToken()
                },
                onFailure = {
                    _uiState.value = AuthUiState.Error(it.message ?: "Error en la autenticación biométrica")
                }
            )
        }
    }

    private fun syncFirebaseToken() {
        Log.d("VIVIA_FCM_DEBUG", "Iniciando petición de token a Firebase...")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("VIVIA_FCM_DEBUG", "Fallo al obtener el token FCM: ${task.exception?.message}")
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("VIVIA_FCM_DEBUG", "📱 FCM TOKEN GENERADO: $token")

            viewModelScope.launch {
                val result = updateFcmTokenUseCase(token)
                result.fold(
                    onSuccess = { Log.d("VIVIA_FCM_DEBUG", "✅ Token guardado en el backend") },
                    onFailure = { e -> Log.e("VIVIA_FCM_DEBUG", "❌ Error al guardar token: ${e.message}") }
                )
            }
        }
    }
}
