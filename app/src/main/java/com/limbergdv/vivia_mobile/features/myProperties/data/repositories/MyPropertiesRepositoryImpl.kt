package com.limbergdv.vivia_mobile.features.myProperties.data.repositories

import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType
import com.limbergdv.vivia_mobile.features.myProperties.domain.repositories.MyPropertiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MyPropertiesRepositoryImpl @Inject constructor(
    // TODO: Agregar api: MyPropertiesApi cuando el backend esté listo
) : MyPropertiesRepository {

    override fun getMyProperties(): Flow<List<Property>> {
        // Stub con datos de ejemplo — reemplazar con llamada real a la API
        return flowOf(
            listOf(
                Property(
                    id            = "1", // <-- ¡Corregido a String!
                    listingType   = ListingType.VENTA,
                    city          = "Mérida",
                    state         = "Yucatán",
                    neighborhood  = "Montejo",
                    address       = "Calle 60 Norte", // <-- Agregado por el nuevo campo
                    propertyType  = PropertyType.CASAS,
                    price         = 2000000.0,
                    landArea      = 2000.0,
                    bedrooms      = 4,
                    bathrooms     = 3,
                    parkingSpaces = 2,
                    title         = "Casa en venta en Montejo",
                    description   = "Hermosa casa en el exclusivo fraccionamiento Montejo con acabados de lujo, amplio jardín y alberca privada.",
                    imageUris     = emptyList()
                ),
                Property(
                    id            = "2", // <-- ¡Corregido a String!
                    listingType   = ListingType.RENTA,
                    city          = "Mérida",
                    state         = "Yucatán",
                    neighborhood  = "Altabrisa",
                    address       = "Avenida Altabrisa", // <-- Agregado por el nuevo campo
                    propertyType  = PropertyType.PISOS_DEPARTAMENTOS,
                    price         = 18000.0,
                    landArea      = 120.0,
                    bedrooms      = 2,
                    bathrooms     = 2,
                    parkingSpaces = 1,
                    title         = "Departamento en Altabrisa",
                    description   = "Departamento moderno en zona norte de Mérida, cerca de centros comerciales y hospitales.",
                    imageUris     = emptyList()
                ),
                Property(
                    id            = "3", // <-- ¡Corregido a String!
                    listingType   = ListingType.VENTA,
                    city          = "Mérida",
                    state         = "Yucatán",
                    neighborhood  = "García Ginerés",
                    address       = "Calle 17", // <-- Agregado por el nuevo campo
                    propertyType  = PropertyType.CASAS,
                    price         = 4000000.0,
                    landArea      = 500.0,
                    bedrooms      = 5,
                    bathrooms     = 4,
                    parkingSpaces = 3,
                    title         = "Casa colonial en García Ginerés",
                    description   = "Espectacular casa con arquitectura colonial restaurada, 5 habitaciones, jardín tropical y alberca.",
                    imageUris     = emptyList()
                )
            )
        )
    }
}