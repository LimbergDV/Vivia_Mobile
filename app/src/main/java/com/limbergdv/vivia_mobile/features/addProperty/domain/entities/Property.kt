package com.limbergdv.vivia_mobile.features.addProperty.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
    val id: Int = 0,
    val listingType: ListingType,           // VENTA o RENTA
    val city: String,
    val state: String,
    val neighborhood: String,
    val propertyType: PropertyType,
    val price: Double,                      // Precio total (venta) o mensual (renta)
    val landArea: Double,                   // m2
    val bedrooms: Int,
    val bathrooms: Int,
    val parkingSpaces: Int,
    val title: String,
    val description: String,
    val imageUris: List<String> = emptyList()
) : Parcelable

enum class ListingType {
    VENTA, RENTA
}

enum class PropertyType(val label: String) {
    PISOS_DEPARTAMENTOS("Pisos y departamentos"),
    CASAS("Casas"),
    TERRENOS("Terrenos"),
    LOCALES_COMERCIALES("Locales comerciales"),
    OFICINAS("Oficinas"),
    BODEGAS("Bodegas")
}