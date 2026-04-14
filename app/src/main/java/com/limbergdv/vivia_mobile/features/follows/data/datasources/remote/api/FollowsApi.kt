package com.limbergdv.vivia_mobile.features.follows.data.datasources.remote.api

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.dtos.BaseResponse
import com.limbergdv.vivia_mobile.features.follows.domain.entities.Follower
import retrofit2.Response
import retrofit2.http.GET

interface FollowsApi {

    @GET("/lessors/followers")
    suspend fun getFollowers(): Response<BaseResponse<List<Follower>>>

}