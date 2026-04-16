package com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories

import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import kotlinx.coroutines.flow.Flow

interface LesseePropertiesRepository {
    fun observeProperties(): Flow<List<Property>>
    suspend fun sync(): Result<Unit>
    suspend fun getPropertyById(id: String): Result<Property>
}
