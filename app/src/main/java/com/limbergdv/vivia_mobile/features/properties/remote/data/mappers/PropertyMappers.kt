package com.limbergdv.vivia_mobile.features.properties.remote.data.mappers

import com.limbergdv.vivia_mobile.core.database.entities.AddressEntity
import com.limbergdv.vivia_mobile.core.database.entities.PropertyEntity
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.LessorResponseDto
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyResponseDto
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Lessor
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property

fun LessorResponseDto.toDomain(): Lessor {
    return Lessor(
        id = id ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        companyName = companyName,
        phoneNumber = phoneNumber ?: ""
    )
}

fun PropertyResponseDto.toDomain(): Property {
    return Property(
        id = id ?: "",
        title = title ?: "Sin título",
        description = description ?: "",
        price = price ?: 0.0,
        address = address?.address ?: "",
        city = address?.city ?: "",
        state = address?.state ?: "",
        neighborhood = address?.neighborhood ?: "",
        departmentType = departmentType ?: "",
        area = area ?: 0.0,
        roomsNumber = roomsNumber ?: 0,
        bathroomsNumber = bathroomsNumber ?: 0,
        parkingNumber = parkingNumber ?: 0,
        lessorId = lessorId ?: "",
        imageUrls = imageUrls ?: emptyList(),
        lessor = lessor?.toDomain()
    )
}

fun PropertyResponseDto.toEntity(): PropertyEntity {
    return PropertyEntity(
        id             = id ?: "",
        title          = title ?: "Sin título",
        description    = description ?: "",
        price          = price ?: 0.0,
        address        = AddressEntity(
            address      = address?.address ?: "",
            city         = address?.city ?: "",
            state        = address?.state ?: "",
            neighborhood = address?.neighborhood ?: ""
        ),
        departmentType = departmentType ?: "",
        area           = area ?: 0.0,
        roomsNumber    = roomsNumber ?: 0,
        bathroomsNumber = bathroomsNumber ?: 0,
        parkingNumber  = parkingNumber ?: 0,
        lessorId       = lessorId ?: "",
        imageUrls      = imageUrls ?: emptyList()
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
