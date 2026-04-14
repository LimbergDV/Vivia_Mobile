package com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases

import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.LesseePropertiesRepository
import javax.inject.Inject

class SyncLesseePropertiesUseCase @Inject constructor(
    private val repository: LesseePropertiesRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.sync()
}
