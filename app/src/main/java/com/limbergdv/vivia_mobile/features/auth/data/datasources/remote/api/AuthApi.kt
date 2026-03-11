package com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.AuthVerifyRequestDto
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.AuthVerifyResponseDto
import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login/challenge")
    suspend fun authChallenge(): BaseResponse<String>

    @POST("auth/login/verify")
    suspend fun authVerify(@Body request: AuthVerifyRequestDto): BaseResponse<AuthVerifyResponseDto>

}