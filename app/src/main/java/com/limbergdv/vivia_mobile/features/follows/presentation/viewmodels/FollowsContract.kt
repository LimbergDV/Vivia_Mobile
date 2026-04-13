package com.limbergdv.vivia_mobile.features.follows.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.LessorWithFollowStatus

// Estado de la pantalla
data class FollowsState(
    val isLoading: Boolean = false,
    val lessors: List<LessorWithFollowStatus> = emptyList(),
    val error: String? = null
)

// Acciones del usuario
sealed class FollowsEvent {
    object LoadLessors : FollowsEvent()
    data class OnFollowClicked(val companyName: String) : FollowsEvent()
    object ConsumeError : FollowsEvent()
    data class Logout(val onLogoutComplete: () -> Unit) : FollowsEvent()
}