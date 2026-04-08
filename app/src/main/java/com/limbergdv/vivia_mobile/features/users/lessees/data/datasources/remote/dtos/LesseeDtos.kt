package com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos

data class BaseResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
    val status: String?
)

data class LesseeRegisterChallengeRequestDto(
    val username: String,
    val email: String,
    val password: String,
)

data class LesseeRegisterVerifyRequestDto(
    val email: String,
    val credentialResponseJson: String
)

data class LesseeRegisterVerifyResponseDto(
    val id: String,
    val username: String,
    val email: String,
    val password: String
)