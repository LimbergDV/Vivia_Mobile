package com.limbergdv.vivia_mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLesseeScreen
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLessorScreen
import com.limbergdv.vivia_mobile.features.home.presentation.screens.HomeScreen
import com.limbergdv.vivia_mobile.features.home.presentation.screens.LessorOptionScreen
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
                    appNavigator.navigate(AppRoutes.HOME) {
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
                    appNavigator.navigate(AppRoutes.HOME) {
                        // Evita que el usuario regrese al registro presionando "Atrás"
                        popUpTo(AppRoutes.REGISTER_LESSOR) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.LOGIN_LESSOR) {
            LoginLessorScreen(
                onNavigateToRegister = { appNavigator.navigate(AppRoutes.REGISTER_LESSOR) },
                onFingerprintClick = {},
                onNavigateNext = {}
            )
        }

        composable(AppRoutes.LOGIN_LESSEE) {
            LoginLesseeScreen(
                onNavigateToRegister = { appNavigator.navigate(AppRoutes.REGISTER_LESSEE) },
                onFingerprintClick = {},
                onNavigateNext = {}
            )
        }

    }
}