package com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.Lessee

data class LesseeProfileState(
    val isLoading: Boolean = false,
    val lessee: Lessee? = null,
    val error: String? = null
)

sealed class LesseeProfileEvent {
    object LoadProfile : LesseeProfileEvent()
    object ConsumeError : LesseeProfileEvent()
    data class Logout(val onLogoutComplete: () -> Unit) : LesseeProfileEvent()
}
