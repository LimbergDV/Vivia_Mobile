package com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels

// Estado inmutable de la pantalla
data class RegisterLessorState(
    val firstName: String = "",
    val lastName: String = "",
    val companyName: String = "",
    val isLoading: Boolean = false,
    val webAuthnChallenge: String? = null, // Desafío devuelto por el backend
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false
)

// Acciones que el usuario puede realizar
sealed class RegisterLessorEvent {
    data class FirstNameChanged(val firstName: String) : RegisterLessorEvent()
    data class LastNameChanged(val lastName: String) : RegisterLessorEvent()
    data class CompanyNameChanged(val companyName: String) : RegisterLessorEvent()
    object RegisterClicked : RegisterLessorEvent()

    // Resultados del hardware biométrico
    data class OnBiometricSuccess(val credentialResponseJson: String) : RegisterLessorEvent()
    data class OnBiometricError(val error: String) : RegisterLessorEvent()

    object ConsumeChallenge : RegisterLessorEvent() // Limpia el desafío tras usarlo
    object ConsumeError : RegisterLessorEvent()     // Limpia el error tras mostrarlo
}