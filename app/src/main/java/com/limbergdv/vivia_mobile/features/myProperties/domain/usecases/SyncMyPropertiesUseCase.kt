package com.limbergdv.vivia_mobile.features.myProperties.domain.usecases

import com.limbergdv.vivia_mobile.features.myProperties.domain.repositories.MyPropertiesRepository
import javax.inject.Inject

class SyncMyPropertiesUseCase @Inject constructor(
    private val repository: MyPropertiesRepository
) {
    suspend operator fun invoke(companyName: String): Result<Unit> {
        return repository.syncMyProperties(companyName)
    }
}
