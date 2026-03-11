package com.limbergdv.vivia_mobile.core.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.limbergdv.vivia_mobile.core.hardware.data.BiometricServiceImpl
import com.limbergdv.vivia_mobile.core.hardware.domain.BiometricService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HardwareProviderModule {

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareBindingModule {

    @Binds
    @Singleton
    abstract fun bindBiometricService(
        biometricServiceImpl: BiometricServiceImpl
    ): BiometricService
}