package com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorResponseDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.RegisterLessorChallengeDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.VerifyLessorRegistrationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LessorApi {
    @POST("/lessors/register/challenge")
    suspend fun getRegistrationChallenge(@Body request: RegisterLessorChallengeDto): Response<BaseResponse<String>>

    @POST("/lessors/register/verify")
    suspend fun verifyRegistration(@Body request: VerifyLessorRegistrationDto): Response<BaseResponse<String>>

    @GET("/lessors")
    suspend fun getAllLessors(): Response<BaseResponse<List<LessorResponseDto>>>
}
