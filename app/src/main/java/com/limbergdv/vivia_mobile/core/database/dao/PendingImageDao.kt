package com.limbergdv.vivia_mobile.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.limbergdv.vivia_mobile.core.database.entities.PendingImageEntity

@Dao
interface PendingImageDao {
    @Insert
    suspend fun insert(pendingImage: PendingImageEntity)

    @Query("SELECT * FROM pending_images WHERE propertyId = :propertyId")
    suspend fun getImagesByProperty(propertyId: String): List<PendingImageEntity>

    @Query("SELECT * FROM pending_images ORDER BY createdAt ASC")
    suspend fun getAllPendingImages(): List<PendingImageEntity>

    @Delete
    suspend fun delete(pendingImage: PendingImageEntity)

    @Query("DELETE FROM pending_images WHERE propertyId = :propertyId")
    suspend fun deleteByPropertyId(propertyId: String)
}