package com.limbergdv.vivia_mobile.features.follows.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.follows.domain.entities.Follower

data class FollowersState(
    val isLoading: Boolean = false,
    val followers: List<Follower> = emptyList(),
    val error: String? = null
)

sealed class FollowersEvent {
    object LoadFollowers : FollowersEvent()
    object ConsumeError : FollowersEvent()
}