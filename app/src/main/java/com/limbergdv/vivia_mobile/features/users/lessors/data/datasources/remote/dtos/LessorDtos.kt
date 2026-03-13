package com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos

data class BaseResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
    val status: String?
)

data class LessorRegisterChallengeRequestDto(
    val firstName: String,
    val lastName: String,
    val companyName: String
)

/*data class LessorRegisterChallengeResponseDto(
    val challenge: String
)*/

data class LessorRegisterVerifyRequestDto(
    val companyName: String,
    val credentialResponseJson: String
)

data class  LessorRegisterVerifyResponseDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val companyName: String
)

data class LessorResponseDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val companyName: String
)