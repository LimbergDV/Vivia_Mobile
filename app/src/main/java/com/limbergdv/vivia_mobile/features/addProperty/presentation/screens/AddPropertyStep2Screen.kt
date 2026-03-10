package com.limbergdv.vivia_mobile.features.addProperty.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Shower
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.NumberSelector
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.SectionHeader
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.ViviaButton
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.ViviaTextField

private val BEDROOM_OPTIONS  = listOf("1","2","3","4","5","6","7","8","9","10","10+")
private val BATHROOM_OPTIONS  = listOf("1","2","3","4","5","6","6+")
private val PARKING_OPTIONS   = listOf("1","2","3","4","5","6","6+")

@Composable
fun AddPropertyStep2Screen(
    uiState: AddPropertyUiState,
    onBedroomsChange: (Int) -> Unit,
    onBathroomsChange: (Int) -> Unit,
    onParkingSpacesChange: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ── Habitaciones ─────────────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Outlined.Bed, contentDescription = null) },
            title = "Habitaciones"
        )

        NumberSelector(
            options = BEDROOM_OPTIONS,
            selectedValue = uiState.bedrooms,
            onSelected = onBedroomsChange
        )

        // ── Baños ────────────────────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Outlined.Shower, contentDescription = null) },
            title = "Baños"
        )

        NumberSelector(
            options = BATHROOM_OPTIONS,
            selectedValue = uiState.bathrooms,
            onSelected = onBathroomsChange
        )

        // ── Estacionamiento ──────────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null) },
            title = "Espacios De Estacionamiento"
        )

        NumberSelector(
            options = PARKING_OPTIONS,
            selectedValue = uiState.parkingSpaces,
            onSelected = onParkingSpacesChange
        )

        // ── Título ───────────────────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Outlined.Sell, contentDescription = null) },
            title = "Título Breve"
        )

        ViviaTextField(
            value = uiState.title,
            onValueChange = onTitleChange,
            placeholder = "Añade un título breve..."
        )

        // ── Descripción ──────────────────────────────────────────────────────
        SectionHeader(
            icon = { Icon(Icons.Outlined.Sell, contentDescription = null) },
            title = "Descripción De La Propiedad"
        )

        ViviaTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            placeholder = "Añade una descripción...",
            singleLine = false,
            minLines = 5,
            modifier = Modifier.height(140.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ViviaButton(
            text = "Siguiente: Añadir Imágenes",
            onClick = onNext
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}