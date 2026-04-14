package com.limbergdv.vivia_mobile.features.users.lessees.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.core.navigation.AppRoutes
import com.limbergdv.vivia_mobile.features.follows.presentation.components.LesseeBottomBar
import com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels.LesseeProfileEvent
import com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels.LesseeProfileViewModel
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.screens.ProfileInfoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LesseeProfileScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: LesseeProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(LesseeProfileEvent.ConsumeError)
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.onEvent(LesseeProfileEvent.Logout(onLogoutComplete = onLogout))
                }) { Text("Sí, cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            LesseeBottomBar(
                currentRoute = "profile",
                onHomeClick = { onNavigate(AppRoutes.LESSEE_HOME) },
                onFollowsClick = { onNavigate(AppRoutes.FOLLOWS_LIST) },
                onSearchClick = { onNavigate("search") },
                onProfileClick = { /* ya estamos aquí */ },
                onLogoutClick = { showLogoutDialog = true }
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading && state.lessee == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF0D3B4F)
                )
            } else if (state.lessee != null) {
                val lessee = state.lessee!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF0D3B4F),
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    ProfileInfoCard(
                        title = "Usuario",
                        value = lessee.username,
                        icon = Icons.Default.Person
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInfoCard(
                        title = "Correo electrónico",
                        value = lessee.email,
                        icon = Icons.Default.Email
                    )
                }
            }
        }
    }
}
