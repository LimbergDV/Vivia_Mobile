package com.limbergdv.vivia_mobile.features.properties.remote.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.limbergdv.vivia_mobile.R
import com.limbergdv.vivia_mobile.core.navigation.AppRoutes
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.components.ViviaBottomBar
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels.MyPropertiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPropertiesScreen(
    viewModel: MyPropertiesViewModel = hiltViewModel(),
    onPropertyClick: (String) -> Unit,
    onAddPropertyClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearErrorMessage()
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
                    viewModel.logout(onLogoutComplete = onLogout)
                }) { Text("Sí, cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF0F2F5),
        bottomBar = {
            ViviaBottomBar(
                currentRoute    = "myProperties",
                onHomeClick     = { onNavigate(AppRoutes.MY_PROPERTIES) },
                onProfileClick  = { onNavigate(AppRoutes.LESSOR_PROFILE) },
                onAddClick      = onAddPropertyClick,
                onFollowersClick = { onNavigate(AppRoutes.LESSOR_FOLLOWERS) },
                onLogoutClick = { showLogoutDialog = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPropertyClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar propiedad")
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isSyncing,
            onRefresh    = { viewModel.syncProperties() },
            modifier     = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Header ────────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0F2F5))
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(text = "Hola!", fontSize = 16.sp, color = Color.DarkGray)
                    Text(
                        text = "Mis Propiedades",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                // ── Lista ─────────────────────────────────────────────────────
                if (uiState.properties.isEmpty() && !uiState.isSyncing) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🏠", fontSize = 48.sp)
                            Text(
                                text = "Aún no tienes propiedades",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Presiona + para agregar la primera",
                                fontSize = 13.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.properties, key = { it.id }) { property ->
                            PropertyItem(
                                property = property,
                                onClick  = { onPropertyClick(property.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Card de propiedad con imagen ──────────────────────────────────────────────
@Composable
fun PropertyItem(
    property: Property,
    onClick: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {

            // ── Imagen de portada ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFEEF2F7))
            ) {
                if (property.imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model              = property.imageUrls.first(),
                        contentDescription = property.title,
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Crop,
                        placeholder        = painterResource(R.drawable.ic_vivia_logo_only),
                        error              = painterResource(R.drawable.ic_vivia_logo_only)
                    )
                } else {
                    // Placeholder cuando no hay imagen
                    Icon(
                        imageVector     = Icons.Outlined.Home,
                        contentDescription = null,
                        modifier        = Modifier
                            .size(56.dp)
                            .align(Alignment.Center),
                        tint            = Color(0xFF0D3B4F).copy(alpha = 0.25f)
                    )
                }

                // Badge con tipo de propiedad
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0D3B4F))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = property.departmentType,
                        color      = Color.White,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // ── Info ──────────────────────────────────────────────────────────
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text       = property.title,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 17.sp,
                    color      = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text     = "${property.neighborhood}, ${property.city}, ${property.state}",
                    color    = Color.Gray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text       = "$${"%,.2f".format(property.price)}",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ── Chips de características ──────────────────────────────────
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBadge("${property.roomsNumber} hab")
                    InfoBadge("${property.bathroomsNumber} baños")
                    InfoBadge("${property.area.toInt()} m²")
                    if (property.parkingNumber > 0) {
                        InfoBadge("${property.parkingNumber} est.")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoBadge(label: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF475569))
    }
}