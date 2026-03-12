package com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessors.domain.entities.Lessor
import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import javax.inject.Inject

class GetAllLessorsUseCase @Inject constructor(
    private val repository: LessorRepository
) {
    suspend operator fun invoke(): Result<List<Lessor>> {
        return repository.getAllLessors()
    }
}