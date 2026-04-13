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
    val firstName: String,
    val lastName: String,
    val companyName: String,
    val password: String,
    //@SerializedName("phone_number")
    val phoneNumber: String,
    val credentialResponseJson: String
)

data class LessorResponseDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val companyName: String
)
