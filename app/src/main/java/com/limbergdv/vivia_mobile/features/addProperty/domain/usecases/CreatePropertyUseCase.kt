package com.limbergdv.vivia_mobile.features.addProperty.domain.usecases

import android.util.Log
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import javax.inject.Inject

class CreatePropertyUseCase @Inject constructor(
    private val repository: AddPropertyRepository
) {
    suspend operator fun invoke(property: Property): Result<Property> {

        // Log para ver exactamente qué valores llegan
        Log.d("VIVIA_PROPERTY_DEBUG", "=== Validando propiedad ===")
        Log.d("VIVIA_PROPERTY_DEBUG", "title: '${property.title}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "city: '${property.city}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "state: '${property.state}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "neighborhood: '${property.neighborhood}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "price: ${property.price}")
        Log.d("VIVIA_PROPERTY_DEBUG", "landArea: ${property.landArea}")
        Log.d("VIVIA_PROPERTY_DEBUG", "bedrooms: ${property.bedrooms}")
        Log.d("VIVIA_PROPERTY_DEBUG", "bathrooms: ${property.bathrooms}")
        Log.d("VIVIA_PROPERTY_DEBUG", "parkingSpaces: ${property.parkingSpaces}")
        Log.d("VIVIA_PROPERTY_DEBUG", "description: '${property.description}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "imageUris count: ${property.imageUris.size}")

        if (property.title.isBlank()) {
            return Result.failure(Exception("El título no puede estar vacío"))
        }
        if (property.city.isBlank()) {
            return Result.failure(Exception("La ciudad es obligatoria"))
        }
        if (property.state.isBlank()) {
            return Result.failure(Exception("El estado es obligatorio"))
        }
        if (property.neighborhood.isBlank()) {
            return Result.failure(Exception("La colonia es obligatoria"))
        }
        if (property.price <= 0) {
            return Result.failure(Exception("El precio debe ser mayor a 0"))
        }
        if (property.landArea <= 0) {
            return Result.failure(Exception("El área del terreno debe ser mayor a 0"))
        }

        return try {
            val created = repository.createProperty(property)
            Result.success(created)
        } catch (e: Exception) {
            Log.e("VIVIA_PROPERTY_DEBUG", "Error al crear propiedad: ${e.message}")
            Result.failure(e)
        }
    }
}