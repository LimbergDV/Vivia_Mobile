package com.limbergdv.vivia_mobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.limbergdv.vivia_mobile.core.database.converters.PropertyConverters
import com.limbergdv.vivia_mobile.core.database.dao.PendingImageDao
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDao
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDraftDao
import com.limbergdv.vivia_mobile.core.database.dao.LesseePropertyDao
import com.limbergdv.vivia_mobile.core.database.entities.PendingImageEntity
import com.limbergdv.vivia_mobile.core.database.entities.PropertyDraftEntity
import com.limbergdv.vivia_mobile.core.database.entities.PropertyEntity
import com.limbergdv.vivia_mobile.core.database.entities.LesseePropertyEntity

@Database(
    entities = [
        PropertyDraftEntity::class,
        PropertyEntity::class,
        PendingImageEntity::class,
        LesseePropertyEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(PropertyConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propertyDraftDao(): PropertyDraftDao
    abstract fun propertyDao(): PropertyDao
    abstract fun pendingImageDao(): PendingImageDao
    abstract fun lesseePropertyDao(): LesseePropertyDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE property_draft ADD COLUMN address TEXT NOT NULL DEFAULT ''"
                )
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE properties RENAME TO properties_old")
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `properties` (
                        `id` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `price` REAL NOT NULL,
                        `addr_address` TEXT NOT NULL DEFAULT '',
                        `addr_city` TEXT NOT NULL DEFAULT '',
                        `addr_state` TEXT NOT NULL DEFAULT '',
                        `addr_neighborhood` TEXT NOT NULL DEFAULT '',
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
                database.execSQL(
                    """
                    INSERT INTO properties (id, title, description, price,
                        addr_address, addr_city, addr_state, addr_neighborhood,
                        departmentType, area, roomsNumber, bathroomsNumber,
                        parkingNumber, lessorId, imageUrls)
                    SELECT id, title, description, price,
                        address, city, state, neighborhood,
                        departmentType, area, roomsNumber, bathroomsNumber,
                        parkingNumber, lessorId, imageUrls
                    FROM properties_old
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE properties_old")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar tabla pending_images
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `pending_images` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `propertyId` TEXT NOT NULL, 
                        `imageUri` TEXT NOT NULL, 
                        `createdAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                // Agregar tabla tenant_properties
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `tenant_properties` (
                        `id` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `price` REAL NOT NULL,
                        `addr_address` TEXT NOT NULL DEFAULT '',
                        `addr_city` TEXT NOT NULL DEFAULT '',
                        `addr_state` TEXT NOT NULL DEFAULT '',
                        `addr_neighborhood` TEXT NOT NULL DEFAULT '',
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