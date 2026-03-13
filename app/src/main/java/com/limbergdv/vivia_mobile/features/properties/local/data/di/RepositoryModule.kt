package com.limbergdv.vivia_mobile.features.properties.local.data.di

import com.limbergdv.vivia_mobile.features.properties.local.data.datasources.remote.api.AddPropertyApi
import com.limbergdv.vivia_mobile.features.properties.local.data.repositories.AddPropertyRepositoryImpl
import com.limbergdv.vivia_mobile.features.properties.local.domain.repositories.AddPropertyRepository
import com.limbergdv.vivia_mobile.features.properties.local.domain.usecases.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AddPropertyApiModule {

    @Provides
    @Singleton
    fun provideAddPropertyApi(retrofit: Retrofit): AddPropertyApi =
        retrofit.create(AddPropertyApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AddPropertyRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAddPropertyRepository(
        impl: AddPropertyRepositoryImpl
    ): AddPropertyRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AddPropertyUseCaseModule {

    @Provides
    fun provideAddPropertyUseCases(
        repository: AddPropertyRepository
    ): AddPropertyUseCases = AddPropertyUseCases(
        getDraft       = GetDraftUseCase(repository),
        saveDraft      = SaveDraftUseCase(repository),
        clearDraft     = ClearDraftUseCase(repository),
        createProperty = CreatePropertyUseCase(repository)
    )
}