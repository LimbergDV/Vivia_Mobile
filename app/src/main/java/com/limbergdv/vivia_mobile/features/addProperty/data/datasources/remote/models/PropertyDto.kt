package com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models

data class PropertyDto(
    val id: Int?,
    val listing_type: String?,       // "VENTA" | "RENTA"
    val city: String?,
    val state: String?,
    val neighborhood: String?,
    val property_type: String?,
    val price: Double?,
    val land_area: Double?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val parking_spaces: Int?,
    val title: String?,
    val description: String?,
    val image_urls: List<String>?
)

data class CreatePropertyRequest(
    val listing_type: String,
    val city: String,
    val state: String,
    val neighborhood: String,
    val property_type: String,
    val price: Double,
    val land_area: Double,
    val bedrooms: Int,
    val bathrooms: Int,
    val parking_spaces: Int,
    val title: String,
    val description: String
)