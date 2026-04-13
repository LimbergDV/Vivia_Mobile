package com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessees.domain.entities.LessorWithFollowStatus
import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class GetLessorsWithFollowStatusUseCase @Inject constructor(
    private val repository: LesseeRepository
) {
    suspend operator fun invoke(): Result<List<LessorWithFollowStatus>> {
        return repository.getLessorsWithFollowStatus()
    }
}
