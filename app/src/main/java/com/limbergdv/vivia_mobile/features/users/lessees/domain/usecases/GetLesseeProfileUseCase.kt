package com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.Lessee
import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class GetLesseeProfileUseCase @Inject constructor(
    private val repository: LesseeRepository
) {
    suspend operator fun invoke(): Result<Lessee> = repository.getLesseeProfile()
}
