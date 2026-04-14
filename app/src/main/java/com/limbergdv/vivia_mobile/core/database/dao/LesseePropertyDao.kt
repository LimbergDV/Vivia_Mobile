package com.limbergdv.vivia_mobile.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.limbergdv.vivia_mobile.core.database.entities.LesseePropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LesseePropertyDao {

    @Query("SELECT * FROM tenant_properties")
    fun observeAll(): Flow<List<LesseePropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(properties: List<LesseePropertyEntity>)

    @Query("DELETE FROM tenant_properties")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(properties: List<LesseePropertyEntity>) {
        deleteAll()
        insertAll(properties)
    }
}
