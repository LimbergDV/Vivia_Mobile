package com.limbergdv.vivia_mobile.features.addProperty.domain.usecases

import com.limbergdv.vivia_mobile.features.addProperty.domain.usecases.GetDraftUseCase
import com.limbergdv.vivia_mobile.features.addProperty.domain.usecases.SaveDraftUseCase
import com.limbergdv.vivia_mobile.features.addProperty.domain.usecases.ClearDraftUseCase

/**
 * Mismo patrón que PostUseCases en el repo de referencia.
 * El ViewModel recibe un único objeto en lugar de inyecciones individuales.
 */
data class AddPropertyUseCases(
    val getDraft: GetDraftUseCase,
    val saveDraft: SaveDraftUseCase,
    val clearDraft: ClearDraftUseCase,
    val createProperty: CreatePropertyUseCase
)