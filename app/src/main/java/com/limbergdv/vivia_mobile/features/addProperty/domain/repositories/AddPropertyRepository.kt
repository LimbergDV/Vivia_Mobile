package com.limbergdv.vivia_mobile.features.addProperty.domain.repositories


import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property

interface AddPropertyRepository {
    suspend fun createProperty(property: Property): Property
}