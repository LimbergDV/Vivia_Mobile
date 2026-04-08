package com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.AuthUiState

data class RegisterLesseeState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
)

sealed class RegisterLesseeEvent {
    data class UsernameChanged(val username: String) : RegisterLesseeEvent()
    data class EmailChanged(val email: String) : RegisterLesseeEvent()
    data class PasswordChanged(val password: String) : RegisterLesseeEvent()
    object RegisterClicked : RegisterLesseeEvent()
    object ResetUiState : RegisterLesseeEvent()
}
