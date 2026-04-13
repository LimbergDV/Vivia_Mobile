package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

data class LoginLesseeState(
    val email: String = "",
    val password: String = "",
)

sealed class LoginLesseeEvent {
    data class EmailChanged(val email: String) : LoginLesseeEvent()
    data class PasswordChanged(val password: String) : LoginLesseeEvent()
    object TraditionalLoginClicked : LoginLesseeEvent()
    object BiometricLoginClicked : LoginLesseeEvent()
    object ResetUiState : LoginLesseeEvent()
}
