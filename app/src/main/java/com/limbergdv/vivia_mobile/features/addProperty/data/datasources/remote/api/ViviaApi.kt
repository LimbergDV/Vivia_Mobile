package com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models.BasePropertyResponse
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models.CreatePropertyRequest
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models.PropertyDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AddPropertyApi {

    @Multipart
    @POST("properties")
    suspend fun createProperty(
        @Part("property") request: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): BasePropertyResponse<PropertyDto>

    @Multipart
    @POST("properties")
    suspend fun createPropertyWithoutImages(
        @Part("property") request: RequestBody
    ): BasePropertyResponse<PropertyDto>
}