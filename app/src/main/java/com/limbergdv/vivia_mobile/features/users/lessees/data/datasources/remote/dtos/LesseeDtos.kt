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
