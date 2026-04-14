package com.limbergdv.vivia_mobile.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_images")
data class PendingImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: String,
    val imageUri: String, // URI local del archivo
    val createdAt: Long = System.currentTimeMillis()
)