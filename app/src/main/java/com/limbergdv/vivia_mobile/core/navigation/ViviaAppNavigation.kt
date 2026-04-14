package com.limbergdv.vivia_mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLesseeScreen
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLessorScreen
import com.limbergdv.vivia_mobile.features.follows.presentation.screens.FollowersScreen
import com.limbergdv.vivia_mobile.features.follows.presentation.screens.FollowsScreen
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.screens.LesseePropertiesScreen
import com.limbergdv.vivia_mobile.features.users.lessees.presentation.screens.LesseeProfileScreen
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.screens.LessorProfileScreen
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
    startDestination: String = AppRoutes.AUTH_GRAPH,
    userType: TokenDataStore.UserType? = null
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
        val homeGraphStart = when (userType) {
            TokenDataStore.UserType.LESSEE -> AppRoutes.LESSEE_HOME
            TokenDataStore.UserType.LESSOR -> AppRoutes.MY_PROPERTIES
            null -> AppRoutes.MY_PROPERTIES // Default
        }

        navigation(
            startDestination = homeGraphStart,
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

            composable(AppRoutes.LESSEE_HOME) {
                LesseePropertiesScreen(
                    onNavigate = { destination -> appNavigator.navigate(destination) },
                    onLogout = {}
                )
            }

            composable(AppRoutes.FOLLOWS_LIST) {
                FollowsScreen(
                    onNavigate = { destination -> appNavigator.navigate(destination) },
                    onLogout = {}
                )
            }

            composable(AppRoutes.LESSOR_FOLLOWERS) {
                FollowersScreen(
                    onNavigateBack = { appNavigator.popBackStack() }
                )
            }

            composable(AppRoutes.LESSOR_PROFILE) {
                LessorProfileScreen(
                    onNavigate = { destination -> appNavigator.navigate(destination) },
                    onLogout = {
                        // El MainActivity manejará el logout al observar el cambio de tokens
                    }
                )
            }

            composable(AppRoutes.LESSEE_PROFILE) {
                LesseeProfileScreen(
                    onNavigate = { destination -> appNavigator.navigate(destination) },
                    onLogout = {}
                )
            }

            composable("property_details/{propertyId}") { backStackEntry ->
                PropertyDetailScreen(
                    onBack = {
                        appNavigator.navigate(AppRoutes.MY_PROPERTIES) {
                            popUpTo(AppRoutes.MY_PROPERTIES) { inclusive = true }
                        }
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