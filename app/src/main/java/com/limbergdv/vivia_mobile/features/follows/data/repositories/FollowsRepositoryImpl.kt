package com.limbergdv.vivia_mobile.features.follows.data.repositories

import com.limbergdv.vivia_mobile.features.follows.data.datasources.remote.api.FollowsApi
import com.limbergdv.vivia_mobile.features.follows.domain.entities.Follower
import com.limbergdv.vivia_mobile.features.follows.domain.repositories.FollowsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FollowsRepositoryImpl @Inject constructor(
    private val followsApi: FollowsApi
) : FollowsRepository {

    override suspend fun getFollowers(): Result<List<Follower>> {
        return try {
            val response = followsApi.getFollowers()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Error desconocido en el servidor"))
                }
            } else {
                Result.failure(Exception("Error en la conexión con el servidor (HTTP ${response.code()})"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión a internet", e))
        } catch (e: HttpException) {
            Result.failure(Exception("Error en el servidor", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}