package com.limbergdv.vivia_mobile.features.myProperties.data.mappers

import com.limbergdv.vivia_mobile.core.database.entities.PropertyEntity
import com.limbergdv.vivia_mobile.features.myProperties.data.datasources.remote.dtos.PropertyResponseDto
import com.limbergdv.vivia_mobile.features.myProperties.domain.entities.Property

fun PropertyResponseDto.toEntity(): PropertyEntity {
    return PropertyEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        address = address,
        city = city,
        state = state,
        neighborhood = neighborhood,
        departmentType = departmentType,
        area = area,
        roomsNumber = roomsNumber,
        bathroomsNumber = bathroomsNumber,
        parkingNumber = parkingNumber,
        lessorId = lessorId,
        imageUrls = imageUrls
    )
}

fun PropertyEntity.toDomain(): Property {
    return Property(
        id = id,
        title = title,
        description = description,
        price = price,
        address = address,
        city = city,
        state = state,
        neighborhood = neighborhood,
        departmentType = departmentType,
        area = area,
        roomsNumber = roomsNumber,
        bathroomsNumber = bathroomsNumber,
        parkingNumber = parkingNumber,
        lessorId = lessorId,
        imageUrls = imageUrls
    )
}
