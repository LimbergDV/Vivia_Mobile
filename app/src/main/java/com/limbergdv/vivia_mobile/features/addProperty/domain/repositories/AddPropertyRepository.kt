package com.limbergdv.vivia_mobile.features.addProperty.domain.repositories


import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyUiState
import kotlinx.coroutines.flow.Flow

interface AddPropertyRepository {

    // borrador local (room)
    fun getDraft(): Flow<AddPropertyUiState?>
    suspend fun saveDraft(uiState: AddPropertyUiState)
    suspend fun clearDraft()

    // publicación remota a la api
    suspend fun createProperty(property: Property): Property
}