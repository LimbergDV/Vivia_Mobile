package com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.AuthUiState

data class RegisterLessorState(
    val firstName: String = "",
    val lastName: String = "",
    val companyName: String = "",
    val password: String = "",
    val phoneNumber: String = ""
)

sealed class RegisterLessorEvent {
    data class FirstNameChanged(val firstName: String) : RegisterLessorEvent()
    data class LastNameChanged(val lastName: String) : RegisterLessorEvent()
    data class CompanyNameChanged(val companyName: String) : RegisterLessorEvent()
    data class PasswordChanged(val password: String) : RegisterLessorEvent()
    data class PhoneNumberChanged(val phoneNumber: String) : RegisterLessorEvent()
    object RegisterClicked : RegisterLessorEvent()
    object ResetUiState : RegisterLessorEvent()
}
