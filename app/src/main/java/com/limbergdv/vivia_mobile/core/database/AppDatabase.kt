package com.limbergdv.vivia_mobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.limbergdv.vivia_mobile.core.database.converters.PropertyConverters
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
import com.limbergdv.vivia_mobile.core.database.entities.PropertyDraftEntity

@Database(
    entities = [
        PropertyDraftEntity::class,
    ],
    version = 2,                // ← incrementado de 1 a 2
    exportSchema = false
)
@TypeConverters(PropertyConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDraftDao(): PropertyDraftDao

    companion object {
        /**
         * Migración 1→2: añade la columna [address] al borrador.
         * El valor por defecto es '' (cadena vacía) para no romper borradores existentes.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE property_draft ADD COLUMN address TEXT NOT NULL DEFAULT ''"
                )
            }
        }
    }
}