package com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels

data class LoginLesseeState(
    val email: String = "",
    val isLoading: Boolean = false,
    val webAuthnChallenge: String? = null,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false
)

sealed class LoginLesseeEvent {
    data class EmailChanged(val email: String) : LoginLesseeEvent()
    object LoginClicked : LoginLesseeEvent()
    data class OnBiometricSuccess(val credentialResponseJson: String) : LoginLesseeEvent()
    data class OnBiometricError(val error: String) : LoginLesseeEvent()
    object ConsumeChallenge : LoginLesseeEvent()
    object ConsumeError : LoginLesseeEvent()
}