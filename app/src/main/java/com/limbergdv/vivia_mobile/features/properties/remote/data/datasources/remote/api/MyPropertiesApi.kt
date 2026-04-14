package com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyListWrapper
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MyPropertiesApi {
    @GET("properties/lessor")
    suspend fun getPropertiesByLessor(): Response<PropertyListWrapper>

    @DELETE("properties/{id}")
    suspend fun deleteProperty(@Path("id") id: String): Response<BaseResponse<String>>

    @Multipart
    @POST("properties/{id}/images")
    suspend fun uploadImages(
        @Path("id") id: String,
        @Part images: List<MultipartBody.Part>
    ): Response<BaseResponse<PropertyResponseDto>>
}