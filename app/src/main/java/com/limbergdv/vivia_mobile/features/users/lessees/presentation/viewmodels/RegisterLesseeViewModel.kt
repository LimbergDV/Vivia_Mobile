package com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.AuthUiState
import com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases.RegisterLesseeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLesseeViewModel @Inject constructor(
    private val registerLesseeUseCase: RegisterLesseeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterLesseeState())
    val state: StateFlow<RegisterLesseeState> = _state.asStateFlow()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterLesseeEvent) {
        when (event) {
            is RegisterLesseeEvent.UsernameChanged -> _state.update { it.copy(username = event.username) }
            is RegisterLesseeEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is RegisterLesseeEvent.PasswordChanged -> _state.update { it.copy(password = event.password) }
            is RegisterLesseeEvent.RegisterClicked -> { /* should call onRegister(context) from UI */ }
            is RegisterLesseeEvent.ResetUiState -> _uiState.value = AuthUiState.Idle
        }
    }

    fun onRegister(context: Context) {
        val username = _state.value.username
        val email = _state.value.email
        val password = _state.value.password

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            registerLesseeUseCase(context, username, email, password).fold(
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
