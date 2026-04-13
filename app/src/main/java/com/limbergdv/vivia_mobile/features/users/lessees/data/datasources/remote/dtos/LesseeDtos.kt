package com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos

data class RegisterLesseeChallengeDto(
    val username: String,
    val email: String,
    val password: String
)

data class VerifyLesseeRegistrationDto(
    val email: String,
    val credentialResponseJson: String
)

data class LesseeRegistrationResponseDto(
    val id: String,
    val username: String,
    val email: String
)

data class LessorInfoDto(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val companyName: String?,
    val phoneNumber: String?
)

data class LessorWithFollowStatusDto(
    val lessor: LessorInfoDto?,
    val following: Boolean?
)
