package com.limbergdv.vivia_mobile.core.navigation

import androidx.navigation.NavOptionsBuilder

interface AppNavigator {
    fun navigate(route: String, builder: (NavOptionsBuilder.() -> Unit)? = null)
    fun popBackStack()
}