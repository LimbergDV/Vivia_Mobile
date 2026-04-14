package com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.SearchFilter
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.ObserveLesseePropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LesseeSearchViewModel @Inject constructor(
    private val observeUseCase: ObserveLesseePropertiesUseCase
) : ViewModel() {

    private val _filter = MutableStateFlow(SearchFilter())
    private val _state = MutableStateFlow(LesseeSearchState())
    val state: StateFlow<LesseeSearchState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(observeUseCase(), _filter) { all, filter ->
                val types = all.map { it.departmentType }
                    .filter { it.isNotBlank() }.distinct().sorted()
                val maxPrice = all.maxOfOrNull { it.price }?.coerceAtLeast(1.0) ?: 10_000_000.0
                LesseeSearchState(
                    filteredProperties = applyFilter(all, filter),
                    filter = filter,
                    availableTypes = types,
                    maxPriceInData = maxPrice
                )
            }.collect { _state.value = it }
        }
    }

    fun onEvent(event: LesseeSearchEvent) {
        when (event) {
            is LesseeSearchEvent.UpdateFilter -> _filter.value = event.filter
            is LesseeSearchEvent.ClearFilters -> _filter.value = SearchFilter()
        }
    }

    private fun applyFilter(list: List<Property>, f: SearchFilter): List<Property> =
        list.filter { p ->
            (f.query.isBlank() || listOf(p.title, p.description, p.city, p.state, p.neighborhood)
                .any { it.contains(f.query, ignoreCase = true) }) &&
            (f.city.isBlank() || p.city.contains(f.city, ignoreCase = true)) &&
            (f.state.isBlank() || p.state.contains(f.state, ignoreCase = true)) &&
            p.price >= f.minPrice &&
            (f.maxPrice == Double.MAX_VALUE || p.price <= f.maxPrice) &&
            (f.departmentType.isBlank() || p.departmentType == f.departmentType) &&
            p.roomsNumber >= f.minRooms &&
            p.bathroomsNumber >= f.minBathrooms &&
            p.parkingNumber >= f.minParking
        }
}
