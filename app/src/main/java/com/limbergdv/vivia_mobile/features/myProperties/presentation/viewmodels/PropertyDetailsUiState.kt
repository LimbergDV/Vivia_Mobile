package com.limbergdv.vivia_mobile.features.myProperties.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.myProperties.domain.entities.Property

data class PropertyDetailsUiState(
    val property: Property? = null,
    val isLoading: Boolean = true
)
