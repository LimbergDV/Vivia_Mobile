package com.limbergdv.vivia_mobile.features.users.lessees.domain.entities

data class LessorWithFollowStatus(
    val id: String,
    val firstName: String,
    val lastName: String,
    val companyName: String,
    val phoneNumber: String,
    val isFollowing: Boolean
)
