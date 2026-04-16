package com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.LesseePropertyListWrapper
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LesseePropertiesApi {

    @GET("properties")
    suspend fun getAllProperties(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 100
    ): Response<LesseePropertyListWrapper>

    @GET("properties/{id}")
    suspend fun getPropertyById(@Path("id") id: String): Response<BaseResponse<PropertyResponseDto>>
}
