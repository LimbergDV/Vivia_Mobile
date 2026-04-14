package com.limbergdv.vivia_mobile.features.properties.remote.data.di

import com.limbergdv.vivia_mobile.features.properties.remote.data.datasources.remote.api.LesseePropertiesApi
import com.limbergdv.vivia_mobile.features.properties.remote.data.repositories.LesseePropertiesRepositoryImpl
import com.limbergdv.vivia_mobile.features.properties.remote.domain.repositories.LesseePropertiesRepository
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.ObserveLesseePropertiesUseCase
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.SyncLesseePropertiesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LesseePropertiesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLesseePropertiesRepository(
        impl: LesseePropertiesRepositoryImpl
    ): LesseePropertiesRepository
}

@Module
@InstallIn(SingletonComponent::class)
object LesseePropertiesNetworkModule {

    @Provides
    @Singleton
    fun provideLesseePropertiesApi(retrofit: Retrofit): LesseePropertiesApi =
        retrofit.create(LesseePropertiesApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object LesseePropertiesUseCaseModule {

    @Provides
    fun provideObserveLesseePropertiesUseCase(
        repository: LesseePropertiesRepository
    ): ObserveLesseePropertiesUseCase = ObserveLesseePropertiesUseCase(repository)

    @Provides
    fun provideSyncLesseePropertiesUseCase(
        repository: LesseePropertiesRepository
    ): SyncLesseePropertiesUseCase = SyncLesseePropertiesUseCase(repository)
}
