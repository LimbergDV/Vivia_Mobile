package com.limbergdv.vivia_mobile.features.myProperties.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.myProperties.data.datasources.remote.dtos.PropertyResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MyPropertiesApi {
    @GET("/properties/lessor/{companyName}")
    suspend fun getPropertiesByLessor(
        @Path("companyName") companyName: String
    ): Response<List<PropertyResponseDto>>
}
