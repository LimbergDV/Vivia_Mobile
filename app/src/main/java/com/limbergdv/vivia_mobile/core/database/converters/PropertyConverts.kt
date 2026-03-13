package com.limbergdv.vivia_mobile.core.database.converters

import androidx.room.TypeConverter
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType

class PropertyConverters {

    // ── Lista de imágenes (URIs separadas por coma) ──────────────────────────
    @TypeConverter
    fun fromImageList(images: List<String>): String = images.joinToString(",")

    @TypeConverter
    fun toImageList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(",")

    @TypeConverter
    fun fromListingType(type: ListingType): String = type.name

    @TypeConverter
    fun toListingType(value: String): ListingType = ListingType.valueOf(value)

    @TypeConverter
    fun fromPropertyType(type: PropertyType): String = type.name

    @TypeConverter
    fun toPropertyType(value: String): PropertyType = PropertyType.valueOf(value)
}