package com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyListWrapper
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface MyPropertiesApi {
    @GET("properties/lessor")
    suspend fun getPropertiesByLessor(): Response<PropertyListWrapper>

    @DELETE("properties/{id}")
    suspend fun deleteProperty(@Path("id") id: String): Response<BaseResponse<String>>
}