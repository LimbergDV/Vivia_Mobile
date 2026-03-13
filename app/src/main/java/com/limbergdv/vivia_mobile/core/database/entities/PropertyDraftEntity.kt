package com.limbergdv.vivia_mobile.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType

/**
 * Entidad Room que persiste el borrador del formulario "Agregar Propiedad".
 *
 * Se guarda un único registro (id = DRAFT_ID) que se sobreescribe con cada
 * cambio, igual que un auto-save. Al publicar la propiedad se elimina.
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
    // Guardamos los URIs como String separados por coma via TypeConverter
    val imageUris: List<String> = emptyList(),

    // ── Meta ─────────────────────────────────────────────────────────────────
    val currentStep: Int = 1,
    val lastModified: Long = System.currentTimeMillis()
) {
    companion object {
        const val DRAFT_ID = 1
    }
}