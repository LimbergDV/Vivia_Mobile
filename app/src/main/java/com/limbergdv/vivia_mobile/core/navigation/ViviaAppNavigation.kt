package com.limbergdv.vivia_mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLesseeScreen
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLessorScreen
import com.limbergdv.vivia_mobile.features.follows.presentation.screens.FollowsScreen
import com.limbergdv.vivia_mobile.features.home.presentation.screens.HomeScreen
import com.limbergdv.vivia_mobile.features.home.presentation.screens.LessorOptionScreen
import com.limbergdv.vivia_mobile.features.properties.local.presentation.screens.AddPropertyScreen
import com.limbergdv.vivia_mobile.features.properties.remote.navigation.MyPropertiesRoutes
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.screens.MyPropertiesScreen
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.screens.PropertyDetailScreen
import com.limbergdv.vivia_mobile.features.users.lessees.presentation.screens.RegisterLesseeScreen
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.screens.RegisterLessorScreen

@Composable
fun ViviaAppNavigation(
    appNavigator: AppNavigatorImpl,
    startDestination: String = AppRoutes.AUTH_GRAPH
) {
    val navController = rememberNavController()

    // Enlazamos el NavController de Compose con tu implementación inyectada
    LaunchedEffect(navController) {
        appNavigator.attach(navController)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Grafo de Autenticación (Usuario no logueado)
        navigation(
            startDestination = AppRoutes.HOME,
            route = AppRoutes.AUTH_GRAPH
        ) {
            composable(AppRoutes.HOME) {
                HomeScreen(
                    toOptionsLessor = { appNavigator.navigate(AppRoutes.OPTIONSS_LESSOR) },
                    toLoginLessee = { appNavigator.navigate(AppRoutes.LOGIN_LESSEE) }
                )
            }

            composable(AppRoutes.OPTIONSS_LESSOR) {
                LessorOptionScreen(
                    onNavigateBack = { appNavigator.popBackStack() },
                    toLoginLessor = { appNavigator.navigate(AppRoutes.LOGIN_LESSOR) },
                    toRegisterLessor = { appNavigator.navigate(AppRoutes.REGISTER_LESSOR)}
                )
            }

            composable(AppRoutes.REGISTER_LESSOR) {
                RegisterLessorScreen(
                    onCancelClick = {
                        appNavigator.navigate(AppRoutes.HOME) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateNext = {
                        appNavigator.navigate(AppRoutes.LOGIN_LESSOR) {
                            popUpTo(AppRoutes.REGISTER_LESSOR) { inclusive = true }
                        }
                    }
                )
            }

            composable(AppRoutes.REGISTER_LESSEE) {
                RegisterLesseeScreen(
                    onCancelClick = {
                        appNavigator.navigate(AppRoutes.HOME) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateNext = {
                        appNavigator.navigate(AppRoutes.LOGIN_LESSEE) {
                            popUpTo(AppRoutes.REGISTER_LESSEE) { inclusive = true }
                        }
                    }
                )
            }

            composable(AppRoutes.LOGIN_LESSOR) {
                LoginLessorScreen(
                    onNavigateToRegister = { appNavigator.navigate(AppRoutes.REGISTER_LESSOR) },
                    onNavigateNext = {
                        appNavigator.navigate(AppRoutes.HOME_GRAPH) {
                            popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AppRoutes.LOGIN_LESSEE) {
                LoginLesseeScreen(
                    onNavigateToRegister = { appNavigator.navigate(AppRoutes.REGISTER_LESSEE) },
                    onNavigateNext = {
                        appNavigator.navigate(AppRoutes.FOLLOWS_LIST) {
                            popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // Grafo Principal (Usuario logueado)
        navigation(
            startDestination = AppRoutes.MY_PROPERTIES,
            route = AppRoutes.HOME_GRAPH
        ) {
            composable(AppRoutes.MY_PROPERTIES) {
                MyPropertiesScreen(
                    onNavigate = { destination -> appNavigator.navigate(destination) },
                    onPropertyClick = { propertyId ->
                        appNavigator.navigate("property_details/$propertyId")
                    },
                    onAddPropertyClick = {
                        appNavigator.navigate(AppRoutes.ADD_PROPERTY)
                    }
                )
            }

            composable(AppRoutes.FOLLOWS_LIST) {
                FollowsScreen()
            }

            composable("property_details/{propertyId}") { backStackEntry ->
                PropertyDetailScreen(
                    onBack = {
                        appNavigator.popBackStack()
                    }
                )
            }

            composable(AppRoutes.ADD_PROPERTY) {
                AddPropertyScreen(
                    onNavigateBack = {
                        appNavigator.navigate(AppRoutes.MY_PROPERTIES) {
                            popUpTo(MyPropertiesRoutes.MY_PROPERTIES_LIST) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
