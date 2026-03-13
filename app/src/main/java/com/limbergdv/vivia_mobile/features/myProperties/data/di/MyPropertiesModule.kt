package com.limbergdv.vivia_mobile.features.myProperties.data.di

import com.limbergdv.vivia_mobile.features.myProperties.data.repositories.MyPropertiesRepositoryImpl
import com.limbergdv.vivia_mobile.features.myProperties.domain.repositories.MyPropertiesRepository
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.GetMyPropertiesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MyPropertiesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyPropertiesRepository(
        impl: MyPropertiesRepositoryImpl
    ): MyPropertiesRepository
}

@Module
@InstallIn(SingletonComponent::class)
object MyPropertiesUseCaseModule {

    @Provides
    fun provideGetMyPropertiesUseCase(
        repository: MyPropertiesRepository
    ): GetMyPropertiesUseCase = GetMyPropertiesUseCase(repository)
}