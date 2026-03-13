package com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LesseeRegisterVerifyResponseDto
import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.Lessee

fun LesseeRegisterVerifyResponseDto.toDomain(): Lessee {
    return Lessee(
        id = this.id,
        username = this.username,
        email = this.email
    )
}
