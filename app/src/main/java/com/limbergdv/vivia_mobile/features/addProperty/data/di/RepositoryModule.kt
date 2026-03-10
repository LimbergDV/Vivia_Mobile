package com.limbergdv.vivia_mobile.features.addProperty.data.di

import com.limbergdv.vivia_mobile.features.addProperty.data.repositories.AddPropertyRepositoryImpl
import com.limbergdv.vivia_mobile.features.addProperty.domain.repositories.AddPropertyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AddPropertyRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAddPropertyRepository(
        impl: AddPropertyRepositoryImpl
    ): AddPropertyRepository
}

// ─────────────────────────────────────────────────────────────────────────────
// TODO: Descomentar y activar este módulo cuando la API esté lista
// ─────────────────────────────────────────────────────────────────────────────
//
// @Module
// @InstallIn(SingletonComponent::class)
// object AddPropertyNetworkModule {
//
//     @Provides
//     @Singleton
//     fun provideAddPropertyApi(retrofit: Retrofit): AddPropertyApi {
//         return retrofit.create(AddPropertyApi::class.java)
//     }
// }