package com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LesseeRegisterChallengeRequestDto
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LesseeRegisterVerifyRequestDto
import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.dtos.LesseeRegisterVerifyResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LesseeApi {
    @POST("lessees/register/challenge")
    suspend fun lesseeRegisterChallenge(@Body request: LesseeRegisterChallengeRequestDto): BaseResponse<String>

    @POST("lessees/register/verify")
    suspend fun lesseeRegisterVerify(@Body request: LesseeRegisterVerifyRequestDto): BaseResponse<LesseeRegisterVerifyResponseDto>

    @PUT("lessees/me/fcm-token")
    suspend fun updateFcmToken(@Query("token") token: String): BaseResponse<String>

    @POST("lessees/me/follow/{companyName}")
    suspend fun followLessor(@Path("companyName") companyName: String): BaseResponse<String>
}