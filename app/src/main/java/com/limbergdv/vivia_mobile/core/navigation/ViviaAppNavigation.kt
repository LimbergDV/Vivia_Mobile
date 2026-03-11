package com.limbergdv.vivia_mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        startDestination = AppRoutes.REGISTER_LESSOR
    ) {

        composable(AppRoutes.REGISTER_LESSOR) {
            RegisterLessorScreen(
                onCancelClick = {
                    // Acción para cancelar, por ejemplo cerrar la app o volver a un Onboarding
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

        composable(AppRoutes.HOME) {
            // HomeScreen()
        }
    }
}