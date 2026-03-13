package com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases

import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.MyPropertiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyPropertiesUseCase @Inject constructor(
    private val repository: MyPropertiesRepository
) {
    operator fun invoke(): Flow<List<Property>> {
        return repository.observeMyProperties()
    }
}
