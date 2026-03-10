package com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.mappers

import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models.CreatePropertyRequest
import com.limbergdv.vivia_mobile.features.addProperty.data.datasources.remote.models.PropertyDto
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType

fun PropertyDto.toDomain(): Property {
    return Property(
        id            = id ?: 0,
        listingType   = runCatching { ListingType.valueOf(listing_type ?: "") }.getOrDefault(ListingType.VENTA),
        city          = city ?: "",
        state         = state ?: "",
        neighborhood  = neighborhood ?: "",
        propertyType  = runCatching { PropertyType.valueOf(property_type ?: "") }.getOrDefault(PropertyType.PISOS_DEPARTAMENTOS),
        price         = price ?: 0.0,
        landArea      = land_area ?: 0.0,
        bedrooms      = bedrooms ?: 1,
        bathrooms     = bathrooms ?: 1,
        parkingSpaces = parking_spaces ?: 0,
        title         = title ?: "",
        description   = description ?: "",
        imageUris     = image_urls ?: emptyList()
    )
}

fun Property.toRequest(): CreatePropertyRequest {
    return CreatePropertyRequest(
        listing_type   = listingType.name,
        city           = city,
        state          = state,
        neighborhood   = neighborhood,
        property_type  = propertyType.name,
        price          = price,
        land_area      = landArea,
        bedrooms       = bedrooms,
        bathrooms      = bathrooms,
        parking_spaces = parkingSpaces,
        title          = title,
        description    = description
    )
}


