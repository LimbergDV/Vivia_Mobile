package com.limbergdv.vivia_mobile.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureNavGraph {
    fun register(builder: NavGraphBuilder, navController: NavHostController)
}