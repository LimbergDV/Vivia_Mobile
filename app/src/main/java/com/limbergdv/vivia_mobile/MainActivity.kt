package com.limbergdv.vivia_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.limbergdv.vivia_mobile.core.navigation.AppNavigatorImpl
import com.limbergdv.vivia_mobile.core.navigation.AppRoutes
import com.limbergdv.vivia_mobile.core.navigation.ViviaAppNavigation
import com.limbergdv.vivia_mobile.core.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appNavigator: AppNavigatorImpl
    
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authState by viewModel.authState.collectAsState()

            AppTheme {
                when (authState) {
                    is AuthState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
                    is AuthState.Authenticated -> {
                        ViviaAppNavigation(
                            appNavigator = appNavigator,
                            startDestination = AppRoutes.HOME_GRAPH
                        )
                    }
                    is AuthState.Unauthenticated -> {
                        ViviaAppNavigation(
                            appNavigator = appNavigator,
                            startDestination = AppRoutes.AUTH_GRAPH
                        )
                    }
                }
            }
        }
    }
}
