package com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.models.BasePropertyResponse
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.models.CreatePropertyRequest
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.models.PropertyDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AddPropertyApi {

    @POST("properties")
    suspend fun createProperty(
        @Body request: CreatePropertyRequest
    ): BasePropertyResponse<PropertyDto>

    @Multipart
    @POST("properties/{id}/images")
    suspend fun uploadImages(
        @Path("id") propertyId: String,
        @Part images: List<MultipartBody.Part>
    ): Response<BasePropertyResponse<PropertyDto>>
}