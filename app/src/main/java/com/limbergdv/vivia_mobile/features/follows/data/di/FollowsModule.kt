package com.limbergdv.vivia_mobile.features.follows.data.di

import com.limbergdv.vivia_mobile.features.follows.data.datasources.remote.api.FollowsApi
import com.limbergdv.vivia_mobile.features.follows.data.repositories.FollowsRepositoryImpl
import com.limbergdv.vivia_mobile.features.follows.domain.repositories.FollowsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FollowsModule {

    @Binds
    @Singleton
    abstract fun bindFollowsRepository(
        impl: FollowsRepositoryImpl
    ): FollowsRepository

    companion object {
        @Provides
        @Singleton
        fun provideFollowsApi(retrofit: Retrofit): FollowsApi {
            return retrofit.create(FollowsApi::class.java)
        }
    }
}