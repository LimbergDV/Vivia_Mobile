package com.limbergdv.vivia_mobile.features.users.lessors.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.core.navigation.AppRoutes
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.components.ViviaBottomBar
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels.LessorProfileEvent
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels.LessorProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessorProfileScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: LessorProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(LessorProfileEvent.ConsumeError)
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
                    viewModel.onEvent(LessorProfileEvent.Logout(onLogoutComplete = onLogout))
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
            ViviaBottomBar(
                currentRoute = "profile",
                onHomeClick = { onNavigate(AppRoutes.MY_PROPERTIES) },
                onProfileClick = { /* Ya estamos aquí */ },
                onAddClick = { onNavigate(AppRoutes.ADD_PROPERTY) },
                onFollowersClick = { onNavigate(AppRoutes.LESSOR_FOLLOWERS) },
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
            if (state.isLoading && state.lessor == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF0D3B4F)
                )
            } else if (state.lessor != null) {
                val lessor = state.lessor!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar / Icono circular
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

                    // Información del Perfil
                    ProfileInfoCard(
                        title = "Nombre Completo",
                        value = "${lessor.firstName} ${lessor.lastName}",
                        icon = Icons.Default.Person
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInfoCard(
                        title = "Empresa",
                        value = lessor.companyName,
                        icon = Icons.Default.Business
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInfoCard(
                        title = "Teléfono",
                        value = lessor.phoneNumber,
                        icon = Icons.Default.Phone
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(title: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF0D3B4F),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}