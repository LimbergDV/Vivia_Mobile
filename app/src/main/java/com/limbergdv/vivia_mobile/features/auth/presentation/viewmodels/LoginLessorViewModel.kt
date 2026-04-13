package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.BiometricLoginUseCase
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.LessorLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginLessorViewModel @Inject constructor(
    private val lessorLoginUseCase: LessorLoginUseCase,
    private val biometricLoginUseCase: BiometricLoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginLessorState())
    val state: StateFlow<LoginLessorState> = _state.asStateFlow()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginLessorEvent) {
        when (event) {
            is LoginLessorEvent.CompanyNameChanged -> _state.update { it.copy(companyName = event.companyName) }
            is LoginLessorEvent.PasswordChanged -> _state.update { it.copy(password = event.password) }
            is LoginLessorEvent.TraditionalLoginClicked -> loginTraditional()
            is LoginLessorEvent.BiometricLoginClicked -> { /* This should call onBiometricLogin(context) from UI */ }
            is LoginLessorEvent.ResetUiState -> _uiState.value = AuthUiState.Idle
        }
    }

    private fun loginTraditional() {
        val companyName = _state.value.companyName
        val password = _state.value.password

        if (companyName.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Compañía y contraseña son requeridos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            lessorLoginUseCase(companyName, password).fold(
                onSuccess = {
                    _uiState.value = AuthUiState.Success
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
            biometricLoginUseCase(context, TokenDataStore.UserType.LESSOR).fold(
                onSuccess = {
                    _uiState.value = AuthUiState.Success
                },
                onFailure = {
                    _uiState.value = AuthUiState.Error(it.message ?: "Error en la autenticación biométrica")
                }
            )
        }
    }
}
