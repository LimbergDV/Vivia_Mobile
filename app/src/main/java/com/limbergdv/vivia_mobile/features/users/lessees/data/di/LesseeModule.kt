package com.limbergdv.vivia_mobile.features.users.lessees.data.di

import com.limbergdv.vivia_mobile.features.users.lessees.data.datasources.remote.api.LesseeApi
import com.limbergdv.vivia_mobile.features.users.lessees.data.repositories.LesseeRepositoryImpl
import com.limbergdv.vivia_mobile.features.users.lessees.domain.repositories.LesseeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LesseeApiModule {

    @Provides
    @Singleton
    fun provideLesseeApi(retrofit: Retrofit): LesseeApi {
        return retrofit.create(LesseeApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LesseeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLesseeRepository(
        lesseeRepositoryImpl: LesseeRepositoryImpl
    ): LesseeRepository
}