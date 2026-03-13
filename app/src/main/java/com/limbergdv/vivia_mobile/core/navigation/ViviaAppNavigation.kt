package com.limbergdv.vivia_mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
fun ViviaAppNavigation(appNavigator: AppNavigatorImpl) {
    val navController = rememberNavController()

    // Enlazamos el NavController de Compose con tu implementación inyectada
    LaunchedEffect(navController) {
        appNavigator.attach(navController)
    }

    // Definimos el startDestination hacia nuestra vista de pruebas
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
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
                    // Cuando el registro sea exitoso, navegamos al home
                    appNavigator.navigate(AppRoutes.LOGIN_LESSOR) {
                        // Evita que el usuario regrese al registro presionando "Atrás"
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
                    // Cuando el registro sea exitoso, navegamos al home
                    appNavigator.navigate(AppRoutes.LOGIN_LESSEE) {
                        // Evita que el usuario regrese al registro presionando "Atrás"
                        popUpTo(AppRoutes.REGISTER_LESSOR) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.LOGIN_LESSOR) {
            LoginLessorScreen(
                onNavigateToRegister = { appNavigator.navigate(AppRoutes.REGISTER_LESSOR) },
                onNavigateNext = {
                    // Al loguearse exitosamente, limpiamos toda la pila de navegación (popUpTo(0))
                    // para que "Mis Propiedades" sea la nueva pantalla base y no pueda volver atrás al Login.
                    appNavigator.navigate(AppRoutes.MY_PROPERTIES) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.LOGIN_LESSEE) {
            LoginLesseeScreen(
                onNavigateToRegister = { appNavigator.navigate(AppRoutes.REGISTER_LESSEE) },
                onNavigateNext = {
                    // Cambiamos HOME por FOLLOWS_LIST y limpiamos la pila
                    appNavigator.navigate(AppRoutes.FOLLOWS_LIST) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.FOLLOWS_LIST) {
            FollowsScreen()
        }

        composable(AppRoutes.MY_PROPERTIES) {
            MyPropertiesScreen(
                onNavigate = { destination -> appNavigator.navigate(destination) },
                onPropertyClick = { propertyId ->
                    // Navega a los detalles pasando el ID de la propiedad seleccionada
                    appNavigator.navigate("property_details/$propertyId")
                },
                onAddPropertyClick = {
                    // Aquí asumimos que tienes una ruta para crear propiedades, por ejemplo AppRoutes.ADD_PROPERTY
                    appNavigator.navigate(AppRoutes.ADD_PROPERTY)
                }
            )
        }

        // Agregamos la ruta dinámica para la vista de detalles que implementaste en la Fase 3
        composable("property_details/{propertyId}") { backStackEntry ->
            // La vista de detalles misma extraerá el ID mediante el SavedStateHandle de su ViewModel,
            // pero el Navigation Graph debe saber cómo recibir el argumento en la URL.
            PropertyDetailScreen(
                onBack = {
                    appNavigator.popBackStack()
                }
            )
        }

        composable(AppRoutes.ADD_PROPERTY) {
            AddPropertyScreen(
                onNavigateBack = {
                    // 2. Navegamos a My Properties y limpiamos la pila para forzar recarga
                    navController.navigate(AppRoutes.MY_PROPERTIES) {
                        popUpTo(MyPropertiesRoutes.MY_PROPERTIES_LIST) { inclusive = true }
                    }
                }
            )
        }
    }
}