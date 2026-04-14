package com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos

import com.google.gson.annotations.SerializedName

data class RegisterLessorChallengeDto(
    val firstName: String,
    val lastName: String,
    val companyName: String,
    val password: String,
    //@SerializedName("phone_number")
    val phoneNumber: String
)

data class VerifyLessorRegistrationDto(
    val companyName: String,
    val credentialResponseJson: String
)

data class LessorRegistrationResponseDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val companyName: String,
    val phoneNumber: String
)

data class LessorResponseDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val companyName: String,
    val phoneNumber: String? = null
)
