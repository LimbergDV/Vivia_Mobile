package com.limbergdv.vivia_mobile.features.addProperty.data.datasources.local.mapper

import android.net.Uri
import com.limbergdv.vivia_mobile.core.database.entities.PropertyDraftEntity
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyUiState

// Entity → UiState  (al cargar el borrador guardado)
fun PropertyDraftEntity.toUiState(): AddPropertyUiState = AddPropertyUiState(
    listingType    = listingType,
    city           = city,
    state          = state,
    neighborhood   = neighborhood,
    address        = address,
    propertyType   = propertyType,
    price          = price,
    landArea       = landArea,
    bedrooms       = bedrooms,
    bathrooms      = bathrooms,
    parkingSpaces  = parkingSpaces,
    title          = title,
    description    = description,
    selectedImages = imageUris.map { Uri.parse(it) },
    currentStep    = currentStep
)

// UiState → Entity  (al guardar el borrador)
fun AddPropertyUiState.toDraftEntity(): PropertyDraftEntity = PropertyDraftEntity(
    listingType    = listingType,
    city           = city,
    state          = state,
    neighborhood   = neighborhood,
    address        = address,
    propertyType   = propertyType,
    price          = price,
    landArea       = landArea,
    bedrooms       = bedrooms,
    bathrooms      = bathrooms,
    parkingSpaces  = parkingSpaces,
    title          = title,
    description    = description,
    imageUris      = selectedImages.map { it.toString() },
    currentStep    = currentStep,
    lastModified   = System.currentTimeMillis()
)