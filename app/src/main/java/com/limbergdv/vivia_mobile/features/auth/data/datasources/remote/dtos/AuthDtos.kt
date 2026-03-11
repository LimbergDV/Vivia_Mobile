package com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos

data class BaseResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
    val status: String?
)

data class AuthVerifyRequestDto(
    val credentialResponseJson: String
)

data class AuthVerifyResponseDto(
    val token: String
)