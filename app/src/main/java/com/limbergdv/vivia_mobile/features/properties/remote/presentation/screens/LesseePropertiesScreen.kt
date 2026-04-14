package com.limbergdv.vivia_mobile.features.properties.remote.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.core.navigation.AppRoutes
import com.limbergdv.vivia_mobile.features.follows.presentation.components.LesseeBottomBar
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels.LesseePropertiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LesseePropertiesScreen(
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: LesseePropertiesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(lifecycle) {
        lifecycle.currentStateFlow.collect { state ->
            if (state == Lifecycle.State.RESUMED) viewModel.sync()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    if (showLogoutDialog) {
        LesseeLogoutDialog(
            onConfirm = { showLogoutDialog = false; viewModel.logout(onComplete = onLogout) },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF0F2F5),
        bottomBar = {
            LesseeBottomBar(
                currentRoute = "home",
                onHomeClick = {},
                onFollowsClick = { onNavigate(AppRoutes.FOLLOWS_LIST) },
                onSearchClick = { onNavigate("search") },
                onProfileClick = { onNavigate(AppRoutes.LESSEE_PROFILE) },
                onLogoutClick = { showLogoutDialog = true }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isSyncing,
            onRefresh = { viewModel.sync() },
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LesseePropertiesHeader()
                LesseePropertiesContent(
                    properties = uiState.properties,
                    isSyncing = uiState.isSyncing
                )
            }
        }
    }
}

@Composable
private fun LesseePropertiesHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F2F5))
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(text = "Hola!", fontSize = 16.sp, color = Color.DarkGray)
        Text(
            text = "Propiedades disponibles",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
    }
}

@Composable
private fun LesseePropertiesContent(
    properties: List<Property>,
    isSyncing: Boolean
) {
    if (properties.isEmpty() && !isSyncing) {
        LesseeEmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(properties, key = { it.id }) { property ->
                PropertyItem(property = property, onClick = {})
            }
        }
    }
}

@Composable
private fun LesseeEmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("🏠", fontSize = 48.sp)
            Text(
                text = "No hay propiedades disponibles",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun LesseeLogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Sí, cerrar sesión") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
