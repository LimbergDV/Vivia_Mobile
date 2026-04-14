package com.limbergdv.vivia_mobile.features.follows.domain.usecases

import com.limbergdv.vivia_mobile.features.follows.domain.entities.Follower
import com.limbergdv.vivia_mobile.features.follows.domain.repositories.FollowsRepository
import javax.inject.Inject

class GetFollowersUseCase @Inject constructor(
    private val repository: FollowsRepository
) {
    suspend operator fun invoke(): Result<List<Follower>> {
        return repository.getFollowers()
    }
}