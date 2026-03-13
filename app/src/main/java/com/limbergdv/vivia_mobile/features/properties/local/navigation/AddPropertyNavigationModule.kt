package com.limbergdv.vivia_mobile.features.properties.local.navigation

import com.limbergdv.vivia_mobile.core.navigation.FeatureNavGraph
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class AddPropertyNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindAddPropertyNavGraph(
        impl: AddPropertyNavGraph
    ): FeatureNavGraph
}