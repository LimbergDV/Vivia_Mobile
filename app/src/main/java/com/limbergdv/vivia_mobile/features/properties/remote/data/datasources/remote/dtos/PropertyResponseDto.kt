package com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos

data class AddressResponseDto(
    val address: String?,
    val city: String?,
    val state: String?,
    val neighborhood: String?
)

data class LessorResponseDto(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val companyName: String?,
    val phoneNumber: String?
)

data class PropertyResponseDto(
    val id: String?,
    val title: String?,
    val description: String?,
    val price: Double?,
    val address: AddressResponseDto?,
    val departmentType: String?,
    val area: Double?,
    val roomsNumber: Int?,
    val bathroomsNumber: Int?,
    val parkingNumber: Int?,
    val lessorId: String?,
    val imageUrls: List<String>?,
    val lessor: LessorResponseDto? = null
)

data class PropertyListWrapper(
    val success: Boolean,
    val message: String?,
    val data: List<PropertyResponseDto>?
)
