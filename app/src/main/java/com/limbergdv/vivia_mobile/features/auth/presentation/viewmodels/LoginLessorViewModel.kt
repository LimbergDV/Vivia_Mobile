package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.GetAuthChallengeUseCase
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.VerifyAuthVerifyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginLessorViewModel @Inject constructor(
    private val getChallengeUseCase: GetAuthChallengeUseCase,
    private val verifyUseCase: VerifyAuthVerifyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginLessorState())
    val state: StateFlow<LoginLessorState> = _state.asStateFlow()

    fun onEvent(event: LoginLessorEvent) {
        when (event) {
            is LoginLessorEvent.CompanyNameChanged -> _state.update { it.copy(companyName = event.companyName) }
            is LoginLessorEvent.LoginClicked -> getChallenge()
            is LoginLessorEvent.OnBiometricSuccess -> verifyLogin(event.credentialResponseJson)
            is LoginLessorEvent.OnBiometricError -> _state.update { it.copy(isLoading = false, error = event.error) }
            is LoginLessorEvent.ConsumeChallenge -> _state.update { it.copy(webAuthnChallenge = null) }
            is LoginLessorEvent.ConsumeError -> _state.update { it.copy(error = null) }
        }
    }

    private fun getChallenge() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // 1. Validación del correo
            if (_state.value.companyName.contains("@")) {
                _state.update { it.copy(isLoading = false, error = "El nombre de la companía no es válido") }
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
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message ?: "Error al iniciar sesión") }
                }
            )
        }
    }
}