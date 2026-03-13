package com.limbergdv.vivia_mobile.core.di

import com.limbergdv.vivia_mobile.core.hardware.domain.CameraManager
import com.limbergdv.vivia_mobile.core.hardware.data.AndroidCameraManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import android.content.Context
import androidx.credentials.CredentialManager
import com.limbergdv.vivia_mobile.core.hardware.data.BiometricServiceImpl
import com.limbergdv.vivia_mobile.core.hardware.domain.BiometricService
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindCameraManager(
        impl: AndroidCameraManager
    ): CameraManager

    @Binds
    @Singleton
    abstract fun bindBiometricService(
        biometricServiceImpl: BiometricServiceImpl
    ): BiometricService

}

object HardwareProviderModule {

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }
}