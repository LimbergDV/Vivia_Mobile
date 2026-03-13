package com.limbergdv.vivia_mobile.features.addProperty.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
    val id: String = "", // <- ¡Cambio importante! Ahora es String por el UUID
    val listingType: ListingType, // Puedes conservarlo para lógicas de UI (Renta/Venta) aunque no vaya a la API
    val city: String,
    val state: String,
    val neighborhood: String,
    val address: String = "", // <- Campo agregado para hacer match con tu JSON
    val propertyType: PropertyType,
    val price: Double,
    val landArea: Double,
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