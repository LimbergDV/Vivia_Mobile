package com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorResponseDto
import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor

fun LessorResponseDto.toDomain(): Lessor {
    return Lessor(
        id = id,
        firstName = firstName,
        lastName = lastName,
        companyName = companyName
    )
}
