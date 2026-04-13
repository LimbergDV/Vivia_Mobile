package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

data class LoginLessorState(
    val companyName: String = "",
    val password: String = "",
)

sealed class LoginLessorEvent {
    data class CompanyNameChanged(val companyName: String) : LoginLessorEvent()
    data class PasswordChanged(val password: String) : LoginLessorEvent()
    object TraditionalLoginClicked : LoginLessorEvent()
    object BiometricLoginClicked : LoginLessorEvent()
    object ResetUiState : LoginLessorEvent()
}
