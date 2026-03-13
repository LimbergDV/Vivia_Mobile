package com.limbergdv.vivia_mobile.features.myProperties.data.repositories

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

    override suspend fun syncMyProperties(companyName: String): Result<Unit> {
        return try {
            val response = myPropertiesApi.getPropertiesByLessor(companyName)
            if (response.isSuccessful) {
                val dtos = response.body() ?: emptyList()
                val entities = dtos.map { it.toEntity() }
                propertyDao.insertAll(entities)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
