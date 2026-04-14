package com.limbergdv.vivia_mobile.core.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenant_properties")
data class LesseePropertyEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val price: Double,
    @Embedded(prefix = "addr_") val address: AddressEntity,
    val departmentType: String,
    val area: Double,
    val roomsNumber: Int,
    val bathroomsNumber: Int,
    val parkingNumber: Int,
    val lessorId: String,
    val imageUrls: List<String>
)
