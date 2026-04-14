package com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.SearchFilter

data class LesseeSearchState(
    val filteredProperties: List<Property> = emptyList(),
    val filter: SearchFilter = SearchFilter(),
    val availableTypes: List<String> = emptyList(),
    val maxPriceInData: Double = 10_000_000.0
)

sealed class LesseeSearchEvent {
    data class UpdateFilter(val filter: SearchFilter) : LesseeSearchEvent()
    object ClearFilters : LesseeSearchEvent()
}
