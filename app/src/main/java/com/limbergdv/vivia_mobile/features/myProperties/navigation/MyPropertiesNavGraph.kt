package com.limbergdv.vivia_mobile.features.myProperties.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.limbergdv.vivia_mobile.core.navigation.FeatureNavGraph
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
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
                        // navController aquí es el raíz de MainActivity
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
    var selectedProperty by remember { mutableStateOf<Property?>(null) }

    NavHost(
        navController    = internalNavController,
        startDestination = MyPropertiesRoutes.MY_PROPERTIES_LIST
    ) {
        composable(MyPropertiesRoutes.MY_PROPERTIES_LIST) {
            MyPropertiesScreen(
                onPropertyClick    = { property ->
                    selectedProperty = property
                    internalNavController.navigate(MyPropertiesRoutes.PROPERTY_DETAIL)
                },
                onAddPropertyClick = onAddPropertyClick,
                onNavigate         = { /* TODO: tabs */ }
            )
        }

        composable(MyPropertiesRoutes.PROPERTY_DETAIL) {
            selectedProperty?.let { property ->
                PropertyDetailScreen(
                    property = property,
                    onBack   = { internalNavController.popBackStack() }
                )
            }
        }
    }
}