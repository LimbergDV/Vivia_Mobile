package com.limbergdv.vivia_mobile.features.properties.remote.data.repositories

import android.util.Log
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDao
import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api.MyPropertiesApi
import com.limbergdv.vivia_mobile.features.properties.remote.data.mappers.toDomain
import com.limbergdv.vivia_mobile.features.properties.remote.data.mappers.toEntity
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.MyPropertiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyPropertiesRepositoryImpl @Inject constructor(
    private val propertyDao: PropertyDao,
    private val myPropertiesApi: MyPropertiesApi
) : MyPropertiesRepository {

    override fun observeMyProperties(): Flow<List<Property>> {
        return propertyDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPropertyById(id: String): Flow<Property?> {
        return propertyDao.observeById(id).map { it?.toDomain() }
    }

    override suspend fun syncMyProperties(): Result<Unit> {
        return try {
            Log.d("SyncProperties", "Iniciando GET /properties/lessor...")
            val response = myPropertiesApi.getPropertiesByLessor()

            if (response.isSuccessful) {
                val wrapper = response.body()

                if (wrapper?.success != true || wrapper.data == null) {
                    Log.e("SyncProperties", "Respuesta inválida: ${wrapper?.message}")
                    return Result.failure(Exception(wrapper?.message ?: "Respuesta vacía"))
                }

                val dtos = wrapper.data
                Log.d("SyncProperties", "Propiedades recibidas: ${dtos.size}")

                dtos.forEachIndexed { index, property ->
                    Log.d("SyncProperties", "Propiedad [$index]: ID=${property.id}, Title=${property.title}")
                    Log.d("SyncProperties", "Images [${property.imageUrls?.size ?: 0}]: ${property.imageUrls}")
                }

                val entities = dtos.map { it.toEntity() }
                propertyDao.deleteAll()
                propertyDao.insertAll(entities)
                Log.d("SyncProperties", "Propiedades guardadas en Room: ${entities.size}")

                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("SyncProperties", "Error HTTP ${response.code()}: $errorBody")
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("SyncProperties", "Excepción al sincronizar: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteProperty(id: String): Result<Unit> {
        return try {
            val response = myPropertiesApi.deleteProperty(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    propertyDao.deleteById(id)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(body?.message ?: "Error al eliminar la propiedad"))
                }
            } else {
                Result.failure(Exception("Error en el servidor (HTTP ${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}