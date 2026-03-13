package com.limbergdv.vivia_mobile.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType

/**
 * Entidad Room que persiste el borrador del formulario "Agregar Propiedad".
 * Un único registro (id = DRAFT_ID) se sobreescribe con cada cambio (auto-save).
 * Se elimina al publicar exitosamente.
 *
 * NOTA: Si ya tienes datos en la DB y añades el campo [address], debes
 * incrementar la versión de la DB en AppDatabase (version = 2) y añadir
 * una Migration o usar fallbackToDestructiveMigration() durante desarrollo.
 */
@Entity(tableName = "property_draft")
data class PropertyDraftEntity(
    @PrimaryKey
    val id: Int = DRAFT_ID,

    // ── Paso 1 ───────────────────────────────────────────────────────────────
    val listingType: ListingType = ListingType.VENTA,
    val city: String = "",
    val state: String = "",
    val neighborhood: String = "",
    val address: String = "",                           // ← campo nuevo del contrato
    val propertyType: PropertyType = PropertyType.PISOS_DEPARTAMENTOS,
    val price: String = "",
    val landArea: String = "",

    // ── Paso 2 ───────────────────────────────────────────────────────────────
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val parkingSpaces: Int? = null,
    val title: String = "",
    val description: String = "",

    // ── Paso 3 ───────────────────────────────────────────────────────────────
    val imageUris: List<String> = emptyList(),

    // ── Meta ─────────────────────────────────────────────────────────────────
    val currentStep: Int = 1,
    val lastModified: Long = System.currentTimeMillis()
) {
    companion object {
        const val DRAFT_ID = 1
    }
}