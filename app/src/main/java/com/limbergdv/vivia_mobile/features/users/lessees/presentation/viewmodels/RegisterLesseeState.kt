package com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels

// Estado inmutable de la pantalla de registro de arrendatario
data class RegisterLesseeState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val webAuthnChallenge: String? = null,
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false
)

// Acciones que el usuario puede realizar
sealed class RegisterLesseeEvent {
    data class UsernameChanged(val username: String) : RegisterLesseeEvent()
    data class EmailChanged(val email: String) : RegisterLesseeEvent()

    data class PasswordChanged(val password: String) : RegisterLesseeEvent()
    object RegisterClicked : RegisterLesseeEvent()

    // Resultados del hardware biométrico
    data class OnBiometricSuccess(val credentialResponseJson: String) : RegisterLesseeEvent()
    data class OnBiometricError(val error: String) : RegisterLesseeEvent()

    object ConsumeChallenge : RegisterLesseeEvent()
    object ConsumeError : RegisterLesseeEvent()
}