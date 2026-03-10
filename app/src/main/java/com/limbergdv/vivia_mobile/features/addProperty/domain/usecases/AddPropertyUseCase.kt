package com.limbergdv.vivia_mobile.features.addProperty.domain.usecases


import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import javax.inject.Inject

class CreatePropertyUseCase @Inject constructor(
    private val repository: AddPropertyRepository
) {
    suspend operator fun invoke(property: Property): Result<Property> {
        // Validaciones de dominio
        if (property.title.isBlank()) {
            return Result.failure(Exception("El título no puede estar vacío"))
        }
        if (property.city.isBlank() || property.state.isBlank()) {
            return Result.failure(Exception("La ubicación es obligatoria"))
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
            android.util.Log.e("CreatePropertyUseCase", "Error al crear propiedad", e)
            Result.failure(e)
        }
    }
}