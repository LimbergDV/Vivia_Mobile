package com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/login")
    suspend fun loginTraditional(@Body request: LoginRequestDto): Response<BaseResponse<AuthTokenDto>>

    @POST("/auth/login/challenge")
    suspend fun getLoginChallenge(): Response<BaseResponse<String>>

    @POST("/auth/login/verify")
    suspend fun verifyLogin(@Body request: VerifyLoginRequestDto): Response<BaseResponse<AuthTokenDto>>

    @POST("/auth/refresh")
    fun refreshToken(@Body request: RefreshTokenRequestDto): Call<BaseResponse<AuthTokenDto>>

    @POST("/auth/logout")
    suspend fun logout(): Response<BaseResponse<String>>

}
