package com.limbergdv.vivia_mobile.features.addProperty.data.repositories

import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import javax.inject.Inject


class AddPropertyRepositoryImpl @Inject constructor(
    // TODO: Descomentar cuando la API esté lista
    // private val api: AddPropertyApi
) : AddPropertyRepository {

    override suspend fun createProperty(property: Property): Property {
        // TODO: Reemplazar con llamada real cuando la API esté lista:
        //
        // val response = api.createProperty(property.toRequest())
        // return response.toDomain()

        // Por ahora simulamos una respuesta exitosa con los mismos datos
        android.util.Log.d("AddPropertyRepository", "Simulando creación de propiedad: ${property.title}")
        return property.copy(id = (1000..9999).random())
    }
}