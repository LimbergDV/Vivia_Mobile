package com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos

data class RegisterLessorChallengeDto(
    val firstName: String,
    val lastName: String,
    val companyName: String,
    val password: String,
    val phoneNumber: String
)

data class VerifyLessorRegistrationDto(
    val credentialResponseJson: String
)

data class LessorResponseDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val companyName: String
)
