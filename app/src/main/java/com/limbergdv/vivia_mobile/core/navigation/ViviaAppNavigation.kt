package com.limbergdv.vivia_mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyScreen
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLesseeScreen
import com.limbergdv.vivia_mobile.features.auth.presentation.screens.LoginLessorScreen
import com.limbergdv.vivia_mobile.features.follows.presentation.screens.FollowsScreen
import com.limbergdv.vivia_mobile.features.home.presentation.screens.HomeScreen
import com.limbergdv.vivia_mobile.features.home.presentation.screens.LessorOptionScreen
import com.limbergdv.vivia_mobile.features.users.lessees.presentation.screens.RegisterLesseeScreen
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.screens.RegisterLessorScreen

@Composable
fun ViviaAppNavigation(appNavigator: AppNavigatorImpl) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        appNavigator.attach(navController)
    }

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
                toRegisterLessor = { appNavigator.navigate(AppRoutes.REGISTER_LESSOR) }
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
                    // ← antes estaba vacío, ahora navega a ADD_PROPERTY
                    appNavigator.navigate(AppRoutes.ADD_PROPERTY) {
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
                    appNavigator.navigate(AppRoutes.FOLLOWS_LIST) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.FOLLOWS_LIST) {
            FollowsScreen(
                onAddPropertyClick = {
                    appNavigator.navigate(AppRoutes.ADD_PROPERTY)
                }
            )
        }

        composable(AppRoutes.ADD_PROPERTY) {
            AddPropertyScreen(
                onNavigateBack = { appNavigator.popBackStack() }
            )
        }
    }
}