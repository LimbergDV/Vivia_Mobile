package com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels

import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property

data class PropertyDetailsUiState(
    val property: Property? = null,
    val isLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val isLessorMode: Boolean = true,
    val error: String? = null
)
