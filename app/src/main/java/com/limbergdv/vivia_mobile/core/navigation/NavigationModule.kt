package com.limbergdv.vivia_mobile.core.navigation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(
        impl: AppNavigatorImpl
    ): AppNavigator
}