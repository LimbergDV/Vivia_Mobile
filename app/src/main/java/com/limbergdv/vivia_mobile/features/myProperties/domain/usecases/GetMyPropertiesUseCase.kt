package com.limbergdv.vivia_mobile.features.myProperties.domain.usecases

import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.myProperties.domain.repositories.MyPropertiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyPropertiesUseCase @Inject constructor(
    private val repository: MyPropertiesRepository
) {
    operator fun invoke(): Flow<List<Property>> = repository.getMyProperties()
}