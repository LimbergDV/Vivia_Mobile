package com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorRegisterVerifyResponseDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorResponseDto
import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor

fun LessorRegisterVerifyResponseDto.toDomain(): Lessor {
    return Lessor(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        companyName = this.companyName
    )
}

fun LessorResponseDto.toDomain(): Lessor {
    return Lessor(
        id = id,
        firstName = firstName,
        lastName = lastName,
        companyName = companyName
    )
}