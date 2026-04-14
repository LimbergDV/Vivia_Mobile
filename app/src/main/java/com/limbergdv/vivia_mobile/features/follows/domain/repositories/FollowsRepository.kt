package com.limbergdv.vivia_mobile.features.follows.domain.repositories

import com.limbergdv.vivia_mobile.features.follows.domain.entities.Follower

interface FollowsRepository {
    suspend fun getFollowers(): Result<List<Follower>>
}