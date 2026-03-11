package com.limbergdv.vivia_mobile.core.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

/**
 * Provee el NavController raíz (el de MainActivity) a cualquier
 * composable del árbol sin necesidad de pasarlo como parámetro en
 * cada nivel. Se configura una sola vez en MainActivity.
 */
val LocalRootNavController = compositionLocalOf<NavHostController> {
    error("No se ha configurado LocalRootNavController. Asegúrate de envolverlo en CompositionLocalProvider en MainActivity.")
}