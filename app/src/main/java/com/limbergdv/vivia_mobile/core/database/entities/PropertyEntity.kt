package com.limbergdv.vivia_mobile.core.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    @Embedded(prefix = "addr_")
    val address: AddressEntity,
    val departmentType: String,
    val area: Double,
    val roomsNumber: Int,
    val bathroomsNumber: Int,
    val parkingNumber: Int,
    val lessorId: String,
    val imageUrls: List<String>
)

data class AddressEntity(
    val address: String,
    val city: String,
    val state: String,
    val neighborhood: String
)