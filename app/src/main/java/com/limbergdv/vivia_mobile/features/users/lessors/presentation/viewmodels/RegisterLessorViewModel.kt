package com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.AuthUiState
import com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases.RegisterLessorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLessorViewModel @Inject constructor(
    private val registerLessorUseCase: RegisterLessorUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterLessorState())
    val state: StateFlow<RegisterLessorState> = _state.asStateFlow()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterLessorEvent) {
        when (event) {
            is RegisterLessorEvent.FirstNameChanged -> _state.update { it.copy(firstName = event.firstName) }
            is RegisterLessorEvent.LastNameChanged -> _state.update { it.copy(lastName = event.lastName) }
            is RegisterLessorEvent.CompanyNameChanged -> _state.update { it.copy(companyName = event.companyName) }
            is RegisterLessorEvent.PasswordChanged -> _state.update { it.copy(password = event.password) }
            is RegisterLessorEvent.PhoneNumberChanged -> _state.update { it.copy(phoneNumber = event.phoneNumber) }
            is RegisterLessorEvent.RegisterClicked -> { /* call onRegister(context) from UI */ }
            is RegisterLessorEvent.ResetUiState -> _uiState.value = AuthUiState.Idle
        }
    }

    fun onRegister(context: Context) {
        val s = _state.value
        if (s.firstName.isBlank() || s.lastName.isBlank() || s.companyName.isBlank() || 
            s.password.isBlank() || s.phoneNumber.isBlank()) {
            _uiState.value = AuthUiState.Error("Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            registerLessorUseCase(
                context = context,
                firstName = s.firstName,
                lastName = s.lastName,
                companyName = s.companyName,
                password = s.password,
                phoneNumber = s.phoneNumber
            ).fold(
                onSuccess = {
                    _uiState.value = AuthUiState.Success
                },
                onFailure = {
                    _uiState.value = AuthUiState.Error(it.message ?: "Error al registrar")
                }
            )
        }
    }
}
