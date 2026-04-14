package com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases

import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.MyPropertiesRepository
import javax.inject.Inject

class DeletePropertyUseCase @Inject constructor(
    private val repository: MyPropertiesRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteProperty(id)
    }
}