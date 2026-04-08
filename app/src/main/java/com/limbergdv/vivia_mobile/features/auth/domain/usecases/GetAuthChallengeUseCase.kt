package com.limbergdv.vivia_mobile.features.auth.domain.usecases

import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class GetAuthChallengeUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Result<String> {
        return repository.getLoginChallenge()
    }
}
