package com.limbergdv.vivia_mobile.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.limbergdv.vivia_mobile.core.database.entities.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties")
    fun observeAll(): Flow<List<PropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(properties: List<PropertyEntity>)

    @Query("SELECT * FROM properties WHERE id = :id")
    fun observeById(id: String): Flow<PropertyEntity>
}