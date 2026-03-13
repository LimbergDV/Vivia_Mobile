package com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyListWrapper
import retrofit2.Response
import retrofit2.http.GET

interface MyPropertiesApi {
    @GET("/properties/lessor")
    suspend fun getPropertiesByLessor(): Response<PropertyListWrapper>
}
