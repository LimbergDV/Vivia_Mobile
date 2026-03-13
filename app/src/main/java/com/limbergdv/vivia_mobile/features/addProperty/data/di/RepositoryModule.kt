package com.limbergdv.vivia_mobile.features.addProperty.data.di

import com.limbergdv.vivia_mobile.features.addProperty.data.repositories.AddPropertyRepositoryImpl
import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import com.limbergdv.vivia_mobile.features.addProperty.domain.usecases.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ── Binding del repositorio ───────────────────────────────────────────────────
@Module
@InstallIn(SingletonComponent::class)
abstract class AddPropertyRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAddPropertyRepository(
        impl: AddPropertyRepositoryImpl
    ): AddPropertyRepository
}

// ── UseCases agrupados ────────────────────────────────────────────────────────
// Mismo patrón que UseCaseModule en el repo de referencia
@Module
@InstallIn(SingletonComponent::class)
object AddPropertyUseCaseModule {

    @Provides
    fun provideAddPropertyUseCases(repository: AddPropertyRepository): AddPropertyUseCases {
        return AddPropertyUseCases(
            getDraft        = GetDraftUseCase(repository),
            saveDraft       = SaveDraftUseCase(repository),
            clearDraft      = ClearDraftUseCase(repository),
            createProperty  = CreatePropertyUseCase(repository)
        )
    }
}