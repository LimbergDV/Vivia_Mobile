package com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.AuthTokenDto
import com.limbergdv.vivia_mobile.features.auth.domain.entities.AuthToken

fun AuthTokenDto.toDomain(): AuthToken {
    return AuthToken(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}
