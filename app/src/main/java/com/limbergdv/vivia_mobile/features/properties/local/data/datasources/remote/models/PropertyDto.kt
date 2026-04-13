package com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.models

data class AddressRequest(
    val address: String,
    val city: String,
    val state: String,
    val neighborhood: String
)

data class CreatePropertyRequest(
    val title: String,
    val description: String,
    val price: Double,
    val address: AddressRequest,
    val departmentType: String,
    val area: Double,
    val roomsNumber: Int,
    val bathroomsNumber: Int,
    val parkingNumber: Int
)

data class BasePropertyResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?,
    val status: String?
)

data class AddressDto(
    val address: String,
    val city: String,
    val state: String,
    val neighborhood: String
)

data class PropertyDto(
    val id: String?,
    val title: String?,
    val description: String?,
    val price: Double?,
    val address: AddressDto?,
    val departmentType: String?,
    val area: Double?,
    val roomsNumber: Int?,
    val bathroomsNumber: Int?,
    val parkingNumber: Int?,
    val lessorId: String?,
    val imageUrls: List<String>?
)