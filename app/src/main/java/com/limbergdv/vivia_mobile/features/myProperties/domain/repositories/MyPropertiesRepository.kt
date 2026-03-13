package com.limbergdv.vivia_mobile.features.myProperties.domain.repositories

import com.limbergdv.vivia_mobile.features.myProperties.domain.entities.Property
import kotlinx.coroutines.flow.Flow

interface MyPropertiesRepository {
    fun observeMyProperties(): Flow<List<Property>>
    fun getPropertyById(id: String): Flow<Property>
    suspend fun syncMyProperties(): Result<Unit>
}
