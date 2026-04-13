package com.limbergdv.vivia_mobile.features.properties.remote.data.mappers

import com.limbergdv.vivia_mobile.core.database.entities.AddressEntity
import com.limbergdv.vivia_mobile.core.database.entities.PropertyEntity
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyResponseDto
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property

fun PropertyResponseDto.toEntity(): PropertyEntity {
    return PropertyEntity(
        id             = id,
        title          = title,
        description    = description,
        price          = price,
        address        = AddressEntity(
            address      = address.address,
            city         = address.city,
            state        = address.state,
            neighborhood = address.neighborhood
        ),
        departmentType = departmentType,
        area           = area,
        roomsNumber    = roomsNumber,
        bathroomsNumber = bathroomsNumber,
        parkingNumber  = parkingNumber,
        lessorId       = lessorId,
        imageUrls      = imageUrls
    )
}

fun PropertyEntity.toDomain(): Property {
    return Property(
        id              = id,
        title           = title,
        description     = description,
        price           = price,
        address         = address.address,
        city            = address.city,
        state           = address.state,
        neighborhood    = address.neighborhood,
        departmentType  = departmentType,
        area            = area,
        roomsNumber     = roomsNumber,
        bathroomsNumber = bathroomsNumber,
        parkingNumber   = parkingNumber,
        lessorId        = lessorId,
        imageUrls       = imageUrls
    )
}