package com.limbergdv.vivia_mobile.features.properties.local.presentation.screens

import android.net.Uri
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.PropertyType

data class AddPropertyUiState(
    // ── Paso 1: Información básica ──────────────────────────────────────────
    val listingType: ListingType = ListingType.VENTA,
    val city: String = "",
    val state: String = "",
    val neighborhood: String = "",
    val address: String = "", // <- Nuevo campo para la dirección
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
    val uploadingImages: Set<Uri> = emptySet(),

    // ── Estado de ubicaciones ───────────────────────────────────────────────
    val availableStates: List<String> = emptyList(),
    val availableMunicipalities: List<String> = emptyList(),

    // ── Estado global ───────────────────────────────────────────────────────
    val currentStep: Int = 1,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)