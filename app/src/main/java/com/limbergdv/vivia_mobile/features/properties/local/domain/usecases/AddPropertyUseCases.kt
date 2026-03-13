package com.limbergdv.vivia_mobile.features.properties.local.domain.usecases

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