package com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos

data class LesseePropertyPageDto(
    val content: List<PropertyResponseDto>?,
    val totalElements: Int,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean
)

data class LesseePropertyListWrapper(
    val success: Boolean,
    val message: String?,
    val status: String?,
    val data: LesseePropertyPageDto?
)
