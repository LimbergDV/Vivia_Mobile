package com.limbergdv.vivia_mobile.features.properties.remote.navigation

import com.limbergdv.vivia_mobile.core.navigation.FeatureNavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class MyPropertiesNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindMyPropertiesNavGraph(
        impl: MyPropertiesNavGraph
    ): FeatureNavGraph
}