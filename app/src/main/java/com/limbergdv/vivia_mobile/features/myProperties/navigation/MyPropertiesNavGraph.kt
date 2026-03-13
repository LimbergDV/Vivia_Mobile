package com.limbergdv.vivia_mobile.features.myProperties.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.limbergdv.vivia_mobile.core.navigation.FeatureNavGraph
import com.limbergdv.vivia_mobile.features.addProperty.navigation.AddPropertyRoutes
import com.limbergdv.vivia_mobile.features.myProperties.presentation.screens.MyPropertiesScreen
import com.limbergdv.vivia_mobile.features.myProperties.presentation.screens.PropertyDetailScreen
import javax.inject.Inject

class MyPropertiesNavGraph @Inject constructor() : FeatureNavGraph {

    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.navigation(
            route            = MyPropertiesRoutes.MY_PROPERTIES_GRAPH,
            startDestination = MyPropertiesRoutes.MY_PROPERTIES_LIST
        ) {
            composable(MyPropertiesRoutes.MY_PROPERTIES_LIST) {
                MyPropertiesInternalNavHost(
                    onAddPropertyClick = {
                        navController.navigate(AddPropertyRoutes.ADD_PROPERTY_GRAPH)
                    }
                )
            }
        }
    }
}

@Composable
private fun MyPropertiesInternalNavHost(
    onAddPropertyClick: () -> Unit
) {
    val internalNavController = rememberNavController()

    NavHost(
        navController    = internalNavController,
        startDestination = MyPropertiesRoutes.MY_PROPERTIES_LIST
    ) {
        composable(MyPropertiesRoutes.MY_PROPERTIES_LIST) {
            MyPropertiesScreen(
                onPropertyClick    = { propertyId ->
                    internalNavController.navigate(MyPropertiesRoutes.createPropertyDetailRoute(propertyId))
                },
                onAddPropertyClick = onAddPropertyClick,
                onNavigate         = { /* TODO: tabs */ }
            )
        }

        composable(
            route = MyPropertiesRoutes.PROPERTY_DETAIL,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) {
            PropertyDetailScreen(
                onBack = { internalNavController.popBackStack() }
            )
        }
    }
}
