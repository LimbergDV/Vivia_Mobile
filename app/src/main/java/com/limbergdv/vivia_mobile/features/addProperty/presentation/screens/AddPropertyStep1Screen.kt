package com.limbergdv.vivia_mobile.features.addProperty.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.*

private val CIUDADES = listOf(
    "Ciudad de México", "Guadalajara", "Monterrey", "Puebla",
    "Mérida", "Tijuana", "León", "Querétaro", "San Luis Potosí", "Aguascalientes"
)

private val ESTADOS = listOf(
    "Aguascalientes", "Baja California", "CDMX", "Chihuahua",
    "Jalisco", "Nuevo León", "Puebla", "Querétaro", "Yucatán", "Sonora"
)

@Composable
fun AddPropertyStep1Screen(
    uiState: AddPropertyUiState,
    onListingTypeChange: (ListingType) -> Unit,
    onCityChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onNeighborhoodChange: (String) -> Unit,
    onPropertyTypeChange: (PropertyType) -> Unit,
    onPriceChange: (String) -> Unit,
    onLandAreaChange: (String) -> Unit,
    onNext: () -> Unit,
    onError: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Estado local de validación — solo se muestra tras intentar avanzar
    var showErrors by remember { mutableStateOf(false) }

    val cityError        = showErrors && uiState.city.isBlank()
    val stateError       = showErrors && uiState.state.isBlank()
    val neighborhoodError = showErrors && uiState.neighborhood.isBlank()
    val priceError       = showErrors && uiState.price.isBlank()
    val landAreaError    = showErrors && uiState.landArea.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Agregar Una Propiedad",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        ListingTypeToggle(
            isVenta = uiState.listingType == ListingType.VENTA,
            onVentaClick = { onListingTypeChange(ListingType.VENTA) },
            onRentaClick = { onListingTypeChange(ListingType.RENTA) }
        )

        SectionHeader(
            icon = { Icon(Icons.Outlined.LocationOn, contentDescription = null) },
            title = "Ubicación"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ViviaDropdown(
                    selectedItem = uiState.city.ifBlank { null },
                    items = CIUDADES,
                    label = "Ciudad",
                    itemLabel = { it },
                    onItemSelected = onCityChange
                )
                if (cityError) ValidationError("Selecciona una ciudad")
            }
            Column(modifier = Modifier.weight(1f)) {
                ViviaDropdown(
                    selectedItem = uiState.state.ifBlank { null },
                    items = ESTADOS,
                    label = "Estado",
                    itemLabel = { it },
                    onItemSelected = onStateChange
                )
                if (stateError) ValidationError("Selecciona un estado")
            }
        }

        Column {
            ViviaTextField(
                value = uiState.neighborhood,
                onValueChange = onNeighborhoodChange,
                placeholder = "Escriba la colonia"
            )
            if (neighborhoodError) ValidationError("Escribe la colonia")
        }

        SectionHeader(
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            title = "Tipo De Propiedad"
        )

        ViviaDropdown(
            selectedItem = uiState.propertyType,
            items = PropertyType.entries,
            label = "Tipo de propiedad",
            itemLabel = { it.label },
            onItemSelected = onPropertyTypeChange
        )

        SectionHeader(
            icon = { Icon(Icons.Outlined.Sell, contentDescription = null) },
            title = if (uiState.listingType == ListingType.VENTA) "Precio Total" else "Renta Mensual"
        )

        Column {
            ViviaTextField(
                value = uiState.price,
                onValueChange = onPriceChange,
                placeholder = "Escriba el precio"
            )
            if (priceError) ValidationError("Escribe el precio")
        }

        SectionHeader(
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            title = "Area Del Terreno"
        )

        Column {
            ViviaTextField(
                value = uiState.landArea,
                onValueChange = onLandAreaChange,
                placeholder = "Escriba el área en m2"
            )
            if (landAreaError) ValidationError("Escribe el área del terreno")
        }

        Spacer(modifier = Modifier.height(8.dp))

        ViviaButton(
            text = "Siguiente: Detalles De La Propiedad",
            onClick = {
                showErrors = true
                val isValid = uiState.city.isNotBlank()
                        && uiState.state.isNotBlank()
                        && uiState.neighborhood.isNotBlank()
                        && uiState.price.isNotBlank()
                        && uiState.landArea.isNotBlank()
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