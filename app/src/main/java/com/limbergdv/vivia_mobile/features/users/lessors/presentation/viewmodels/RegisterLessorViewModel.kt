package com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases.GetLessorRegisterChallengeUseCase
import com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases.VerifyLessorRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLessorViewModel @Inject constructor(
    private val getChallengeUseCase: GetLessorRegisterChallengeUseCase,
    private val verifyRegistrationUseCase: VerifyLessorRegistrationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterLessorState())
    val state: StateFlow<RegisterLessorState> = _state.asStateFlow()

    fun onEvent(event: RegisterLessorEvent) {
        when (event) {
            is RegisterLessorEvent.FirstNameChanged -> _state.update { it.copy(firstName = event.firstName) }
            is RegisterLessorEvent.LastNameChanged -> _state.update { it.copy(lastName = event.lastName) }
            is RegisterLessorEvent.CompanyNameChanged -> _state.update { it.copy(companyName = event.companyName) }
            is RegisterLessorEvent.RegisterClicked -> getChallenge()
            is RegisterLessorEvent.OnBiometricSuccess -> verifyRegistration(event.credentialResponseJson)
            is RegisterLessorEvent.OnBiometricError -> _state.update { it.copy(isLoading = false, error = event.error) }
            is RegisterLessorEvent.ConsumeChallenge -> _state.update { it.copy(webAuthnChallenge = null) }
            is RegisterLessorEvent.ConsumeError -> _state.update { it.copy(error = null) }
        }
    }

    private fun getChallenge() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getChallengeUseCase(
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                companyName = _state.value.companyName
            )

            result.fold(
                onSuccess = { challengeJson ->
                    // Exponemos el desafío; la vista lo observará y abrirá el lector de huellas
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
                companyName = _state.value.companyName,
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