package com.limbergdv.vivia_mobile.features.myProperties.presentation.screens

import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property

data class MyPropertiesUiState(
    val isLoading: Boolean = false,
    val properties: List<Property> = emptyList(),
    val error: String? = null
)