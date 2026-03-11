package com.limbergdv.vivia_mobile.core.di

import com.limbergdv.vivia_mobile.core.hardware.domain.CameraManager
import com.limbergdv.vivia_mobile.core.hardware.data.AndroidCameraManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
}