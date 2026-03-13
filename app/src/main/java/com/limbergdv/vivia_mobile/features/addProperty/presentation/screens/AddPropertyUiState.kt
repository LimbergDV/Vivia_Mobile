package com.limbergdv.vivia_mobile.features.addProperty.presentation.screens

import android.net.Uri
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType


data class AddPropertyUiState(
    // ── Paso 1: Información básica ──────────────────────────────────────────
    val listingType: ListingType = ListingType.VENTA,
    val city: String = "",
    val state: String = "",
    val neighborhood: String = "",
    val propertyType: PropertyType = PropertyType.PISOS_DEPARTAMENTOS,
    val price: String = "",
    val landArea: String = "",

    // ── Paso 2: Detalles de la propiedad ────────────────────────────────────
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val parkingSpaces: Int? = null,
    val title: String = "",
    val description: String = "",

    // ── Paso 3: Imágenes ────────────────────────────────────────────────────
    val selectedImages: List<Uri> = emptyList(),
    val uploadingImages: Set<Uri> = emptySet(),   // URIs en proceso de carga

    // ── Estado global ───────────────────────────────────────────────────────
    val currentStep: Int = 1,                      // 1, 2 o 3
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)