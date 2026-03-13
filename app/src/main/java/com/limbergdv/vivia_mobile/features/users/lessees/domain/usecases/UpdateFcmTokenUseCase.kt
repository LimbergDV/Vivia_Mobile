package com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases

import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import javax.inject.Inject

class UpdateFcmTokenUseCase @Inject constructor(
    private val repository: LesseeRepository
) {
    suspend operator fun invoke(token: String): Result<String> {
        return repository.updateFcmToken(token)
    }
}