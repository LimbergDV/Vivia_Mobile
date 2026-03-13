package com.limbergdv.vivia_mobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.limbergdv.vivia_mobile.core.database.converters.PropertyConverters
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
import com.limbergdv.vivia_mobile.core.database.entities.PropertyDraftEntity

@Database(
    entities = [
        PropertyDraftEntity::class,
        com.limbergdv.vivia_mobile.core.database.entities.PropertyEntity::class,
        // Agregar aquí más entidades en el futuro
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(PropertyConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDraftDao(): PropertyDraftDao
    abstract fun propertyDao(): com.limbergdv.vivia_mobile.core.database.dao.PropertyDao
}