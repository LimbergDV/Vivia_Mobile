package com.limbergdv.vivia_mobile.features.auth.domain.entities

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)
