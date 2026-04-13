package com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LessorWithFollowStatusDto
import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.LessorWithFollowStatus

// Mappers for Lessees

fun LessorWithFollowStatusDto.toDomain(): LessorWithFollowStatus {
    return LessorWithFollowStatus(
        id = lessor?.id ?: "",
        firstName = lessor?.firstName ?: "",
        lastName = lessor?.lastName ?: "",
        companyName = lessor?.companyName ?: "Sin nombre",
        phoneNumber = lessor?.phoneNumber ?: "Sin teléfono",
        isFollowing = following ?: false
    )
}
