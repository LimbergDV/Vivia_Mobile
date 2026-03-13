package com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorRegisterChallengeRequestDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorRegisterVerifyRequestDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorRegisterVerifyResponseDto
import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.dtos.LessorResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LessorApi {
    @POST("lessors/register/challenge")
    suspend fun lessorRegisterChallenge(@Body request: LessorRegisterChallengeRequestDto): BaseResponse<String>

    @POST("lessors/register/verify")
    suspend fun lessorRegisterVerify(@Body request: LessorRegisterVerifyRequestDto): BaseResponse<LessorRegisterVerifyResponseDto>

    @GET("lessors")
    suspend fun getAllLessors(): BaseResponse<List<LessorResponseDto>>
}