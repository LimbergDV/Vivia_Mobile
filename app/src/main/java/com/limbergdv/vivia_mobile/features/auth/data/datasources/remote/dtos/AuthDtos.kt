package com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos

data class BaseResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String,
    val status: String
)

data class AuthTokenDto(
    val accessToken: String,
    val refreshToken: String
)

data class LoginRequestDto(
    val identifier: String,
    val password: String
)

data class RefreshTokenRequestDto(
    val refreshToken: String
)

data class VerifyLoginRequestDto(
    val credentialResponseJson: String
)
