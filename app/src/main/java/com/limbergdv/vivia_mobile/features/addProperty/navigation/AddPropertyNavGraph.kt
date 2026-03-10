package com.limbergdv.vivia_mobile.features.addProperty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.limbergdv.vivia_mobile.core.navigation.FeatureNavGraph
import javax.inject.Inject
import androidx.navigation.compose.composable
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyScreen


class AddPropertyNavGraph @Inject constructor() : FeatureNavGraph {

    override fun register(builder: NavGraphBuilder) {
        builder.navigation(
            route            = AddPropertyRoutes.ADD_PROPERTY_GRAPH,
            startDestination = AddPropertyRoutes.ADD_PROPERTY
        ) {
            composable(AddPropertyRoutes.ADD_PROPERTY) {
                AddPropertyScreen()
            }
        }
    }
}