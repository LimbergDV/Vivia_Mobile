package com.limbergdv.vivia_mobile.features.addProperty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.limbergdv.vivia_mobile.core.navigation.FeatureNavGraph
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyScreen
import javax.inject.Inject

class AddPropertyNavGraph @Inject constructor() : FeatureNavGraph {

    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.navigation(
            route            = AddPropertyRoutes.ADD_PROPERTY_GRAPH,
            startDestination = AddPropertyRoutes.ADD_PROPERTY
        ) {
            composable(AddPropertyRoutes.ADD_PROPERTY) {
                AddPropertyScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}