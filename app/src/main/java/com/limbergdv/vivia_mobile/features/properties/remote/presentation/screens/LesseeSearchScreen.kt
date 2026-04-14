package com.limbergdv.vivia_mobile.features.properties.remote.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.core.navigation.AppRoutes
import com.limbergdv.vivia_mobile.features.follows.presentation.components.LesseeBottomBar
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels.LesseeSearchEvent
import com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels.LesseeSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LesseeSearchScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: LesseeSearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showFilterSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Sí, cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        bottomBar = {
            LesseeBottomBar(
                currentRoute = "search",
                onHomeClick = { onNavigate(AppRoutes.LESSEE_HOME) },
                onFollowsClick = { onNavigate(AppRoutes.FOLLOWS_LIST) },
                onSearchClick = {},
                onProfileClick = { onNavigate(AppRoutes.LESSEE_PROFILE) },
                onLogoutClick = { showLogoutDialog = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchTopBar(
                query = state.filter.query,
                filterCount = state.filter.activeCount,
                onQueryChange = {
                    viewModel.onEvent(LesseeSearchEvent.UpdateFilter(state.filter.copy(query = it)))
                },
                onFilterClick = { showFilterSheet = true }
            )

            if (state.filteredProperties.isEmpty()) {
                SearchEmptyState(isFiltered = state.filter.isActive)
            } else {
                Text(
                    text = "${state.filteredProperties.size} propiedad(es) encontrada(s)",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.filteredProperties, key = { it.id }) { property ->
                        PropertyItem(property = property, onClick = {})
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        LesseeSearchFilterSheet(
            filter = state.filter,
            availableTypes = state.availableTypes,
            maxPriceInData = state.maxPriceInData,
            mexicoLocations = state.mexicoLocations,
            onApply = { viewModel.onEvent(LesseeSearchEvent.UpdateFilter(it)) },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Composable
private fun SearchTopBar(
    query: String,
    filterCount: Int,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Buscar propiedades...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        BadgedBox(badge = {
            if (filterCount > 0) Badge { Text("$filterCount") }
        }) {
            IconButton(
                onClick = onFilterClick,
                modifier = Modifier.background(Color(0xFF0D3B4F), RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Tune, contentDescription = "Filtros", tint = Color.White)
            }
        }
    }
}

@Composable
private fun SearchEmptyState(isFiltered: Boolean) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(if (isFiltered) "🔍" else "🏠", fontSize = 48.sp)
            Text(
                text = if (isFiltered) "Sin resultados para los filtros activos"
                       else "No hay propiedades disponibles",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            if (isFiltered) {
                Text(
                    text = "Intenta cambiar o limpiar los filtros",
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
