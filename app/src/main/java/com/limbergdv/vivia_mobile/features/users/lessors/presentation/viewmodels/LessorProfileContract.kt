package com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor

data class LessorProfileState(
    val isLoading: Boolean = false,
    val lessor: Lessor? = null,
    val error: String? = null
)

sealed class LessorProfileEvent {
    object LoadProfile : LessorProfileEvent()
    object ConsumeError : LessorProfileEvent()
    data class Logout(val onLogoutComplete: () -> Unit) : LessorProfileEvent()
}