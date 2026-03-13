package com.limbergdv.vivia_mobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.limbergdv.vivia_mobile.core.database.converters.PropertyConverters
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDao
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
import com.limbergdv.vivia_mobile.core.database.entities.PropertyDraftEntity
import com.limbergdv.vivia_mobile.core.database.entities.PropertyEntity

@Database(
    entities = [
        PropertyDraftEntity::class,
        PropertyEntity::class,
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(PropertyConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propertyDraftDao(): PropertyDraftDao
    abstract fun propertyDao(): PropertyDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Columna nueva en el borrador
                database.execSQL(
                    "ALTER TABLE property_draft ADD COLUMN address TEXT NOT NULL DEFAULT ''"
                )
                // Tabla nueva de propiedades del arrendador
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `properties` (
                        `id` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `price` REAL NOT NULL,
                        `address` TEXT NOT NULL,
                        `city` TEXT NOT NULL,
                        `state` TEXT NOT NULL,
                        `neighborhood` TEXT NOT NULL,
                        `departmentType` TEXT NOT NULL,
                        `area` REAL NOT NULL,
                        `roomsNumber` INTEGER NOT NULL,
                        `bathroomsNumber` INTEGER NOT NULL,
                        `parkingNumber` INTEGER NOT NULL,
                        `lessorId` TEXT NOT NULL,
                        `imageUrls` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }
    }
}