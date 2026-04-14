package com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases

import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.LesseePropertiesRepository
import javax.inject.Inject

class GetPublicPropertyDetailsUseCase @Inject constructor(
    private val repository: LesseePropertiesRepository
) {
    suspend operator fun invoke(id: String): Result<Property> = repository.getPropertyById(id)
}
