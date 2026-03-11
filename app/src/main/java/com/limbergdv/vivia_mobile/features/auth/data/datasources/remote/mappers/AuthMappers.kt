package com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.AuthVerifyResponseDto
import com.limbergdv.vivia_mobile.features.auth.domain.entities.AuthToken

fun AuthVerifyResponseDto.toDomain(): AuthToken {
    return AuthToken(token = this.token)
}