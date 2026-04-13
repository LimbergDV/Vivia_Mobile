package com.limbergdv.vivia_mobile.features.users.lessors.data.di

import com.limbergdv.vivia_mobile.features.users.lessors.data.datasources.remote.api.LessorApi
import com.limbergdv.vivia_mobile.features.users.lessors.data.repositories.LessorRepositoryImpl
import com.limbergdv.vivia_mobile.features.users.lessors.domain.repositories.LessorRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LessorApiModule {

    @Provides
    @Singleton
    fun provideLessorApi(retrofit: Retrofit): LessorApi {
        return retrofit.create(LessorApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LessorRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLessorRepository(
        lessorRepositoryImpl: LessorRepositoryImpl
    ): LessorRepository
}
