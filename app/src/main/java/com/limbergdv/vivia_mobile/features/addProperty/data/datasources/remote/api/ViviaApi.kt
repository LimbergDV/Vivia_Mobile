package com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.api


import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models.CreatePropertyRequest
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models.PropertyDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AddPropertyApi {

    // Ejemplito de la supuesta api, obviamente cambiará

    @POST("api/v1/properties/")
    suspend fun createProperty(
        @Body request: CreatePropertyRequest
    ): PropertyDto
}