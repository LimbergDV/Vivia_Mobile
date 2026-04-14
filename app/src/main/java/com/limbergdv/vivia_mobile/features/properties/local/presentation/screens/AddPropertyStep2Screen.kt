package com.limbergdv.vivia_mobile.features.properties.local.presentation.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.features.properties.local.presentation.components.*

private val BEDROOM_OPTIONS = listOf("1","2","3","4","5","6","6+")
private val BATHROOM_OPTIONS = listOf("1","2","3","4","5","6","6+")
private val PARKING_OPTIONS  = listOf("1","2","3","4","5","6","6+")

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

    var showErrors by remember { mutableStateOf(false) }

    val bedroomsError    = showErrors && uiState.bedrooms == null
    val bathroomsError   = showErrors && uiState.bathrooms == null
    val parkingError     = showErrors && uiState.parkingSpaces == null
    val titleError       = showErrors && uiState.title.isBlank()
    val descriptionError = showErrors && uiState.description.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionHeader(
            icon = { Icon(Icons.Outlined.Bed, contentDescription = null) },
            title = "Habitaciones"
        )
        NumberSelector(
            options = BEDROOM_OPTIONS,
            selectedValue = uiState.bedrooms,
            onSelected = onBedroomsChange
        )
        if (bedroomsError) ValidationError("Selecciona el número de habitaciones")

        SectionHeader(
            icon = { Icon(Icons.Outlined.Shower, contentDescription = null) },
            title = "Baños"
        )
        NumberSelector(
            options = BATHROOM_OPTIONS,
            selectedValue = uiState.bathrooms,
            onSelected = onBathroomsChange
        )
        if (bathroomsError) ValidationError("Selecciona el número de baños")

        SectionHeader(
            icon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null) },
            title = "Espacios De Estacionamiento"
        )
        NumberSelector(
            options = PARKING_OPTIONS,
            selectedValue = uiState.parkingSpaces,
            onSelected = onParkingSpacesChange
        )
        if (parkingError) ValidationError("Selecciona los espacios de estacionamiento")

        SectionHeader(
            icon = { Icon(Icons.Outlined.Sell, contentDescription = null) },
            title = "Título Breve"
        )
        Column {
            ViviaTextField(
                value = uiState.title,
                onValueChange = onTitleChange,
                placeholder = "Añade un título breve..."
            )
            if (titleError) ValidationError("Escribe un título para la propiedad")
        }

        SectionHeader(
            icon = { Icon(Icons.Outlined.Sell, contentDescription = null) },
            title = "Descripción De La Propiedad"
        )
        Column {
            ViviaTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                placeholder = "Añade una descripción...",
                singleLine = false,
                minLines = 5,
                modifier = Modifier.height(140.dp)
            )
            if (descriptionError) ValidationError("Escribe una descripción de la propiedad")
        }

        Spacer(modifier = Modifier.height(8.dp))

        ViviaButton(
            text = "Siguiente: Añadir Imágenes",
            onClick = {
                showErrors = true
                val isValid = uiState.bedrooms != null
                        && uiState.bathrooms != null
                        && uiState.parkingSpaces != null
                        && uiState.title.isNotBlank()
                        && uiState.description.isNotBlank()
                if (isValid) onNext()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ValidationError(message: String) {
    Text(
        text = "⚠ $message",
        color = Color(0xFFB00020),
        fontSize = 12.sp,
        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
    )
}