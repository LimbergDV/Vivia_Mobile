package com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases

import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.LesseePropertiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLesseePropertiesUseCase @Inject constructor(
    private val repository: LesseePropertiesRepository
) {
    operator fun invoke(): Flow<List<Property>> = repository.observeProperties()
}
