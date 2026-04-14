package com.limbergdv.vivia_mobile.features.properties.remote.data.mappers

import com.limbergdv.vivia_mobile.core.database.entities.AddressEntity
import com.limbergdv.vivia_mobile.core.database.entities.LesseePropertyEntity
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.dtos.PropertyResponseDto
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property

fun PropertyResponseDto.toLesseeEntity(): LesseePropertyEntity = LesseePropertyEntity(
    id = id ?: "",
    title = title ?: "Sin título",
    description = description ?: "",
    price = price ?: 0.0,
    address = AddressEntity(
        address = address?.address ?: "",
        city = address?.city ?: "",
        state = address?.state ?: "",
        neighborhood = address?.neighborhood ?: ""
    ),
    departmentType = departmentType ?: "",
    area = area ?: 0.0,
    roomsNumber = roomsNumber ?: 0,
    bathroomsNumber = bathroomsNumber ?: 0,
    parkingNumber = parkingNumber ?: 0,
    lessorId = lessorId ?: "",
    imageUrls = imageUrls ?: emptyList()
)

fun LesseePropertyEntity.toDomain(): Property = Property(
    id = id,
    title = title,
    description = description,
    price = price,
    address = address.address,
    city = address.city,
    state = address.state,
    neighborhood = address.neighborhood,
    departmentType = departmentType,
    area = area,
    roomsNumber = roomsNumber,
    bathroomsNumber = bathroomsNumber,
    parkingNumber = parkingNumber,
    lessorId = lessorId,
    imageUrls = imageUrls
)
