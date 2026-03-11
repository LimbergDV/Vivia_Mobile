package com.limbergdv.vivia_mobile.features.addProperty.data.repositories

import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.local.mapper.toDraftEntity
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.local.mapper.toUiState
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddPropertyRepositoryImpl @Inject constructor(
    private val draftDao: PropertyDraftDao,
    // TODO: Descomentar cuando la API esté lista
    // private val api: AddPropertyApi
) : AddPropertyRepository {

    // ── Borrador local (Room) ─────────────────────────────────────────────────

    /**
     * Emite el borrador guardado en Room como Flow de UiState.
     * El ViewModel lo colecta igual que PostsRepositoryImpl.getPosts().
     */
    override fun getDraft(): Flow<AddPropertyUiState?> =
        draftDao.getDraft().map { entity -> entity?.toUiState() }

    /**
     * Persiste el estado actual del formulario.
     * Se llama automáticamente al cambiar de paso o al pausar la app.
     */
    override suspend fun saveDraft(uiState: AddPropertyUiState) {
        draftDao.saveDraft(uiState.toDraftEntity())
    }

    /**
     * Elimina el borrador tras publicar exitosamente.
     */
    override suspend fun clearDraft() {
        draftDao.clearDraft()
    }

    // ── Publicación remota (API) ──────────────────────────────────────────────

    override suspend fun createProperty(property: Property): Property {
        // TODO: Reemplazar con llamada real cuando la API esté lista:
        // val response = api.createProperty(property.toRequest())
        // return response.toDomain()
        android.util.Log.d("AddPropertyRepo", "Simulando creación: ${property.title}")
        return property.copy(id = (1000..9999).random())
    }
}