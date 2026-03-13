package com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.models.CreatePropertyRequest
import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.models.PropertyDto
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.PropertyType

fun PropertyDto.toDomain(): Property = Property(
    id            = "",
    listingType   = ListingType.VENTA,
    city          = city ?: "",
    state         = state ?: "",
    neighborhood  = neighborhood ?: "",
    propertyType  = runCatching {
        PropertyType.valueOf(departmentType ?: "")
    }.getOrDefault(PropertyType.PISOS_DEPARTAMENTOS),
    price         = price ?: 0.0,
    landArea      = area ?: 0.0,
    bedrooms      = roomsNumber ?: 1,
    bathrooms     = bathroomsNumber ?: 1,
    parkingSpaces = parkingNumber ?: 0,
    title         = title ?: "",
    description   = description ?: "",
    imageUris     = imageUrls ?: emptyList()
)

fun Property.toRequest(): CreatePropertyRequest = CreatePropertyRequest(
    title           = title,
    description     = description,
    price           = price,
    address         = "$neighborhood, $city, $state",
    city            = city,
    state           = state,
    neighborhood    = neighborhood,
    departmentType  = propertyType.name,
    area            = landArea,
    roomsNumber     = bedrooms,
    bathroomsNumber = bathrooms,
    parkingNumber   = parkingSpaces
)