package com.limbergdv.vivia_mobile.features.auth.data.di

import com.limbergdv.vivia_mobile.features.auth.data.datasources.remote.api.AuthApi
import com.limbergdv.vivia_mobile.features.auth.data.repositories.AuthRepositoryImpl
import com.limbergdv.vivia_mobile.features.auth.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}