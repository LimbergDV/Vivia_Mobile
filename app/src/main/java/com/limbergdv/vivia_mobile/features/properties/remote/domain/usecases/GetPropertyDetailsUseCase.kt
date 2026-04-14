package com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases

import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.MyPropertiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyDetailsUseCase @Inject constructor(
    private val repository: MyPropertiesRepository
) {
    operator fun invoke(id: String): Flow<Property?> {
        return repository.getPropertyById(id)
    }
}
