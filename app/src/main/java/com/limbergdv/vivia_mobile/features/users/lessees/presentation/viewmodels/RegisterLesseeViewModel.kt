package com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases.GetLesseeRegisterChallengeUseCase
import com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases.VerifyLesseeRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLesseeViewModel @Inject constructor(
    private val getChallengeUseCase: GetLesseeRegisterChallengeUseCase,
    private val verifyRegistrationUseCase: VerifyLesseeRegistrationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterLesseeState())
    val state: StateFlow<RegisterLesseeState> = _state.asStateFlow()

    fun onEvent(event: RegisterLesseeEvent) {
        when (event) {
            is RegisterLesseeEvent.UsernameChanged -> _state.update { it.copy(username = event.username) }
            is RegisterLesseeEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is RegisterLesseeEvent.RegisterClicked -> getChallenge()
            is RegisterLesseeEvent.OnBiometricSuccess -> verifyRegistration(event.credentialResponseJson)
            is RegisterLesseeEvent.OnBiometricError -> _state.update { it.copy(isLoading = false, error = event.error) }
            is RegisterLesseeEvent.ConsumeChallenge -> _state.update { it.copy(webAuthnChallenge = null) }
            is RegisterLesseeEvent.ConsumeError -> _state.update { it.copy(error = null) }
        }
    }

    private fun getChallenge() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getChallengeUseCase(
                username = _state.value.username,
                email = _state.value.email
            )

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

    private fun verifyRegistration(credentialResponseJson: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = verifyRegistrationUseCase(
                email = _state.value.email,
                credentialResponseJson = credentialResponseJson
            )

            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isRegistrationSuccessful = true) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message ?: "Error al verificar registro") }
                }
            )
        }
    }
}