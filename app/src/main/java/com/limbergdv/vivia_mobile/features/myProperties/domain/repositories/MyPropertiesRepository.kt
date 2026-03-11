package com.limbergdv.vivia_mobile.features.myProperties.domain.repositories

import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import kotlinx.coroutines.flow.Flow

interface MyPropertiesRepository {
    fun getMyProperties(): Flow<List<Property>>
}