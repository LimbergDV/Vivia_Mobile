package com.limbergdv.vivia_mobile.features.myProperties.data.repositories

import android.util.Log
import com.limbergdv.vivia_mobile.core.database.dao.PropertyDao
import com.limbergdv.vivia_mobile.features.myProperties.data.datasources.remote.api.MyPropertiesApi
import com.limbergdv.vivia_mobile.features.myProperties.data.mappers.toDomain
import com.limbergdv.vivia_mobile.features.myProperties.data.mappers.toEntity
import com.limbergdv.vivia_mobile.features.myProperties.domain.entities.Property
import com.limbergdv.vivia_mobile.features.myProperties.domain.repositories.MyPropertiesRepository
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

    override fun getPropertyById(id: String): Flow<Property> {
        return propertyDao.observeById(id).map { it.toDomain() }
    }

    override suspend fun syncMyProperties(): Result<Unit> {
        return try {
            Log.d("SyncProperties", "1. Iniciando petición HTTP a la API...")
            val response = myPropertiesApi.getPropertiesByLessor()

            if (response.isSuccessful) {
                // 3. Extraemos el campo 'data' del wrapper
                val dtos = response.body()?.data ?: emptyList()
                Log.d("SyncProperties", "2. Petición HTTP exitosa. Propiedades recibidas: ${dtos.size}")

                val entities = dtos.map { it.toEntity() }
                propertyDao.insertAll(entities)
                Log.d("SyncProperties", "3. Propiedades guardadas en Room correctamente.")

                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("SyncProperties", "Error HTTP ${response.code()}: $errorBody")
                Result.failure(Exception("Error en la petición: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Aquí es donde estaba ocurriendo tu error silencioso
            Log.e("SyncProperties", "Excepción crítica al sincronizar: ${e.message}", e)
            Result.failure(e)
        }
    }
}
