package com.limbergdv.vivia_mobile.features.properties.remote.domain.entities

data class Property(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val address: String,
    val city: String,
    val state: String,
    val neighborhood: String,
    val departmentType: String,
    val area: Double,
    val roomsNumber: Int,
    val bathroomsNumber: Int,
    val parkingNumber: Int,
    val lessorId: String,
    val imageUrls: List<String>
)
