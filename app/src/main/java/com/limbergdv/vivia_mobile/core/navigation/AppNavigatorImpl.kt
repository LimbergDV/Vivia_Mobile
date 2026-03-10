package com.limbergdv.vivia_mobile.core.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigatorImpl @Inject constructor() : AppNavigator {

    private var navController: NavHostController? = null

    fun attach(navController: NavHostController) {
        this.navController = navController
    }

    override fun navigate(
        route: String,
        builder: (NavOptionsBuilder.() -> Unit)?
    ) {
        navController?.navigate(route) {
            builder?.invoke(this)
        }
    }

    override fun popBackStack() {
        navController?.popBackStack()
    }
}