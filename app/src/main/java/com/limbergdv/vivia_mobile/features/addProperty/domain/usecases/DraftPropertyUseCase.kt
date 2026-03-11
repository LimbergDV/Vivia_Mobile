package com.limbergdv.vivia_mobile.features.addProperty.domain.usecases

import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Observar borrador
class GetDraftUseCase @Inject constructor(
    private val repository: AddPropertyRepository
) {
    operator fun invoke(): Flow<AddPropertyUiState?> = repository.getDraft()
}

// Guardar borrador
class SaveDraftUseCase @Inject constructor(
    private val repository: AddPropertyRepository
) {
    suspend operator fun invoke(uiState: AddPropertyUiState) = repository.saveDraft(uiState)
}

// Limpiar borrador
class ClearDraftUseCase @Inject constructor(
    private val repository: AddPropertyRepository
) {
    suspend operator fun invoke() = repository.clearDraft()
}