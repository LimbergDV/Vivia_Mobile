package com.limbergdv.vivia_mobile.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.limbergdv.vivia_mobile.core.database.entities.PropertyDraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDraftDao {

    /**
     * Observa el borrador en tiempo real.
     * El ViewModel lo colecta con collectAsStateWithLifecycle() — mismo patrón
     * que PostDao.getAllPosts() en el repo de referencia.
     */
    @Query("SELECT * FROM property_draft WHERE id = ${PropertyDraftEntity.DRAFT_ID}")
    fun getDraft(): Flow<PropertyDraftEntity?>

    /**
     * Inserta o sobreescribe el borrador (REPLACE = upsert).
     * Se llama desde el repositorio cada vez que el usuario modifica un campo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: PropertyDraftEntity)

    /**
     * Elimina el borrador al publicar la propiedad exitosamente.
     */
    @Query("DELETE FROM property_draft WHERE id = ${PropertyDraftEntity.DRAFT_ID}")
    suspend fun clearDraft()
}












