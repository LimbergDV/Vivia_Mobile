package com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LesseeRegistrationResponseDto
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.RegisterLesseeChallengeDto
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.VerifyLesseeRegistrationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LesseeApi {
    @POST("/lessees/register/challenge")
    suspend fun getRegistrationChallenge(@Body request: RegisterLesseeChallengeDto): Response<BaseResponse<String>>

    @POST("/lessees/register/verify")
    suspend fun verifyRegistration(@Body request: VerifyLesseeRegistrationDto): Response<BaseResponse<LesseeRegistrationResponseDto>>

    @PUT("/lessees/me/fcm-token")
    suspend fun updateFcmToken(@Query("token") token: String): Response<BaseResponse<String>>

    @POST("/lessees/me/follow/{companyName}")
    suspend fun followLessor(@Path("companyName") companyName: String): Response<BaseResponse<String>>
}
