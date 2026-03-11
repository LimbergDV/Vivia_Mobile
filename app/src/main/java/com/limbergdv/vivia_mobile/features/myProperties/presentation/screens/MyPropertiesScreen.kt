package com.limbergdv.vivia_mobile.features.myProperties.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.myProperties.presentation.components.*
import com.limbergdv.vivia_mobile.features.myProperties.presentation.viewmodels.MyPropertiesViewModel

@Composable
fun MyPropertiesScreen(
    viewModel: MyPropertiesViewModel = hiltViewModel(),
    onPropertyClick: (Property) -> Unit,
    onAddPropertyClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Filtro local por tipo de propiedad
    var selectedFilter by remember { mutableStateOf("Todas") }
    val filters = listOf("Todas", "Casas", "Depas", "Oficinas")

    val filteredProperties = if (selectedFilter == "Todas") {
        uiState.properties
    } else {
        uiState.properties.filter { it.propertyType.label.contains(selectedFilter, ignoreCase = true) }
    }

    Scaffold(
        containerColor = Color(0xFFF0F2F5),
        bottomBar = {
            ViviaBottomBar(
                currentRoute      = "home",
                onHomeClick       = { onNavigate("home") },
                onSavedClick      = { onNavigate("saved") },
                onAddClick        = onAddPropertyClick,
                onMessagesClick   = { onNavigate("messages") },
                onSettingsClick   = { onNavigate("settings") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // ── Header ───────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F2F5))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hola!",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Mis Propiedades",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = ViviaNavy
                        )
                    }
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ViviaNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("L", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Barra de búsqueda + Filtros ──────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Buscar...", color = Color.Gray) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ViviaNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = "Filtros",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ── Chips de filtro ───────────────────────────────────────────────
            LazyRow(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ViviaNavy,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color.DarkGray
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedFilter == filter,
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Sección todas las propiedades ─────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Todas las propiedades",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Ver Todas ▶",
                    fontSize = 13.sp,
                    color = ViviaNavy,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ViviaNavy)
                    }
                }
                uiState.properties.isEmpty() -> {
                    EmptyPropertiesMessage()
                }
                else -> {
                    // Grid 2 columnas usando LazyRow de filas pares
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(filteredProperties) { property ->
                            PropertyCard(
                                property = property,
                                onClick  = { onPropertyClick(property) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ── Estado vacío ─────────────────────────────────────────────────────────────
@Composable
private fun EmptyPropertiesMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("🏠", fontSize = 48.sp)
        Text(
            text = "Aún no tienes propiedades publicadas",
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Presiona el botón + para agregar tu primera propiedad",
            fontSize = 13.sp,
            color = Color.LightGray
        )
    }
}