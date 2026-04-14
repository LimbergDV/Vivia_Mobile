package com.limbergdv.vivia_mobile.core.database.converters

import android.util.Log
import androidx.room.TypeConverter
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.PropertyType

class PropertyConverters {

    @TypeConverter
    fun fromImageList(images: List<String>): String {
        val result = images.joinToString(",")
        Log.d("PropertyConverters", "fromImageList: Input=${images.size} items, Output=$result")
        return result
    }

    @TypeConverter
    fun toImageList(value: String): List<String> {
        val list = if (value.isBlank()) emptyList() else value.split(",")
        Log.d("PropertyConverters", "toImageList: Input=\"$value\", Output=${list.size} items")
        return list
    }

    @TypeConverter
    fun fromListingType(type: ListingType): String = type.name

    @TypeConverter
    fun toListingType(value: String): ListingType = ListingType.valueOf(value)

    @TypeConverter
    fun fromPropertyType(type: PropertyType): String = type.name

    @TypeConverter
    fun toPropertyType(value: String): PropertyType = PropertyType.valueOf(value)
}