package com.limbergdv.vivia_mobile.features.myProperties.data.di

import com.limbergdv.vivia_mobile.features.myProperties.data.datasources.remote.api.MyPropertiesApi
import com.limbergdv.vivia_mobile.features.myProperties.data.repositories.MyPropertiesRepositoryImpl
import com.limbergdv.vivia_mobile.features.myProperties.domain.repositories.MyPropertiesRepository
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.GetPropertyDetailsUseCase
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.ObserveMyPropertiesUseCase
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.SyncMyPropertiesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
object MyPropertiesNetworkModule {

    @Provides
    @Singleton
    fun provideMyPropertiesApi(retrofit: Retrofit): MyPropertiesApi {
        return retrofit.create(MyPropertiesApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MyPropertiesUseCaseModule {

    @Provides
    fun provideObserveMyPropertiesUseCase(
        repository: MyPropertiesRepository
    ): ObserveMyPropertiesUseCase = ObserveMyPropertiesUseCase(repository)

    @Provides
    fun provideGetPropertyDetailsUseCase(
        repository: MyPropertiesRepository
    ): GetPropertyDetailsUseCase = GetPropertyDetailsUseCase(repository)

    @Provides
    fun provideSyncMyPropertiesUseCase(
        repository: MyPropertiesRepository
    ): SyncMyPropertiesUseCase = SyncMyPropertiesUseCase(repository)
}
