package com.limbergdv.vivia_mobile.features.properties.local.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.limbergdv.vivia_mobile.core.navigation.FeatureNavGraph
import com.limbergdv.vivia_mobile.features.properties.local.presentation.screens.AddPropertyScreen
import com.limbergdv.vivia_mobile.features.properties.remote.navigation.MyPropertiesRoutes
import javax.inject.Inject

class AddPropertyNavGraph @Inject constructor() : FeatureNavGraph {

    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.navigation(
            route            = AddPropertyRoutes.ADD_PROPERTY_GRAPH,
            startDestination = AddPropertyRoutes.ADD_PROPERTY
        ) {
            composable(AddPropertyRoutes.ADD_PROPERTY) {
                AddPropertyScreen(
                    onNavigateBack = {
                        // 2. Navegamos a My Properties y limpiamos la pila para forzar recarga
                        navController.navigate(MyPropertiesRoutes.MY_PROPERTIES_LIST) {
                            popUpTo(MyPropertiesRoutes.MY_PROPERTIES_LIST) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}