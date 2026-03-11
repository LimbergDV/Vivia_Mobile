package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

data class LoginLessorState(
    val companyName: String = "",
    val isLoading: Boolean = false,
    val webAuthnChallenge: String? = null,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false
)

sealed class LoginLessorEvent {
    data class CompanyNameChanged(val companyName: String) : LoginLessorEvent()
    object LoginClicked : LoginLessorEvent()
    data class OnBiometricSuccess(val credentialResponseJson: String) : LoginLessorEvent()
    data class OnBiometricError(val error: String) : LoginLessorEvent()
    object ConsumeChallenge : LoginLessorEvent()
    object ConsumeError : LoginLessorEvent()
}