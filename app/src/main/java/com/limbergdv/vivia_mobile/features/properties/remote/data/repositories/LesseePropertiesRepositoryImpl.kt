package com.limbergdv.vivia_mobile.features.properties.remote.data.repositories

import android.util.Log
import com.limbergdv.vivia_mobile.core.database.dao.LesseePropertyDao
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api.LesseePropertiesApi
import com.limbergdv.vivia_mobile.features.properties.remote.data.mappers.toDomain
import com.limbergdv.vivia_mobile.features.properties.remote.data.mappers.toLesseeEntity
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.LesseePropertiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LesseePropertiesRepositoryImpl @Inject constructor(
    private val dao: LesseePropertyDao,
    private val api: LesseePropertiesApi
) : LesseePropertiesRepository {

    override fun observeProperties(): Flow<List<Property>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun sync(): Result<Unit> = try {
        val response = api.getAllProperties()
        if (!response.isSuccessful) {
            return Result.failure(Exception("Error HTTP ${response.code()}"))
        }
        val wrapper = response.body()
        if (wrapper?.success != true || wrapper.data == null) {
            return Result.failure(Exception(wrapper?.message ?: "Respuesta vacía"))
        }

        val dtos = wrapper.data.content ?: emptyList()
        Log.d("LesseeSync", "Propiedades recibidas: ${dtos.size}")
        
        dtos.forEachIndexed { index, property ->
            Log.d("LesseeSync", "Propiedad [$index]: ID=${property.id}, Title=${property.title}")
            Log.d("LesseeSync", "Images [${property.imageUrls?.size ?: 0}]: ${property.imageUrls}")
        }

        val entities = dtos.map { it.toLesseeEntity() }
        dao.replaceAll(entities)
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("LesseeSync", "Error al sincronizar: ${e.message}", e)
        Result.failure(e)
    }

    override suspend fun getPropertyById(id: String): Result<Property> = try {
        val response = api.getPropertyById(id)
        if (response.isSuccessful) {
            val wrapper = response.body()
            if (wrapper?.success == true && wrapper.data != null) {
                Result.success(wrapper.data.toDomain())
            } else {
                Result.failure(Exception(wrapper?.message ?: "Propiedad no encontrada"))
            }
        } else {
            Result.failure(Exception("Error al obtener detalle (HTTP ${response.code()})"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
