package com.limbergdv.vivia_mobile.features.myProperties.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.myProperties.domain.entities.Property

data class MyPropertiesUiState(
    val properties: List<Property> = emptyList(),
    val isSyncing: Boolean = false,
    val errorMessage: String? = null
)
