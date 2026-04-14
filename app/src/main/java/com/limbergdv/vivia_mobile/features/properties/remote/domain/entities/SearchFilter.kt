package com.limbergdv.vivia_mobile.features.properties.remote.domain.entities

data class SearchFilter(
    val query: String = "",
    val city: String = "",
    val state: String = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = Double.MAX_VALUE,
    val departmentType: String = "",
    val minRooms: Int = 0,
    val minBathrooms: Int = 0,
    val minParking: Int = 0
) {
    val isActive: Boolean get() = query.isNotBlank() || city.isNotBlank() ||
        state.isNotBlank() || minPrice > 0 || maxPrice != Double.MAX_VALUE ||
        departmentType.isNotBlank() || minRooms > 0 || minBathrooms > 0 || minParking > 0

    val activeCount: Int get() = listOf(
        query.isNotBlank(),
        city.isNotBlank() || state.isNotBlank(),
        minPrice > 0 || maxPrice != Double.MAX_VALUE,
        departmentType.isNotBlank(),
        minRooms > 0,
        minBathrooms > 0,
        minParking > 0
    ).count { it }
}
