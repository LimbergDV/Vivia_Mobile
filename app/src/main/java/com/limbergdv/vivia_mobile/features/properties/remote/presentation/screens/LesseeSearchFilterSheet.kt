package com.limbergdv.vivia_mobile.features.properties.remote.presentation.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.core.network.dtos.MexicoState
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.SearchFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LesseeSearchFilterSheet(
    filter: SearchFilter,
    availableTypes: List<String>,
    maxPriceInData: Double,
    mexicoLocations: List<MexicoState>,
    onApply: (SearchFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var local by remember { mutableStateOf(filter) }
    val safeMax = maxPriceInData.toFloat().coerceAtLeast(1f)
    var priceRange by remember {
        val lo = local.minPrice.toFloat().coerceIn(0f, safeMax)
        val hi = if (local.maxPrice == Double.MAX_VALUE) safeMax else local.maxPrice.toFloat().coerceIn(0f, safeMax)
        mutableStateOf(lo..hi)
    }

    var stateExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }

    val selectedState = mexicoLocations.find { it.state == local.state }
    val municipalities = selectedState?.municipalities ?: emptyList()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Filtros", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                TextButton(onClick = { local = SearchFilter(); priceRange = 0f..safeMax }) {
                    Text("Limpiar todo", color = Color(0xFF0D3B4F))
                }
            }
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            if (availableTypes.isNotEmpty()) {
                FilterSectionTitle("Tipo de propiedad")
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableTypes.forEach { type ->
                        FilterChip(
                            selected = local.departmentType == type,
                            onClick = {
                                local = local.copy(
                                    departmentType = if (local.departmentType == type) "" else type
                                )
                            },
                            label = { Text(type) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0D3B4F),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            FilterSectionTitle("Ubicación")
            Spacer(Modifier.height(8.dp))

            // State Selection
            ExposedDropdownMenuBox(
                expanded = stateExpanded,
                onExpandedChange = { stateExpanded = !stateExpanded }
            ) {
                OutlinedTextField(
                    value = local.state,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0D3B4F),
                        focusedLabelColor = Color(0xFF0D3B4F)
                    )
                )
                ExposedDropdownMenu(
                    expanded = stateExpanded,
                    onDismissRequest = { stateExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos") },
                        onClick = {
                            local = local.copy(state = "", city = "")
                            stateExpanded = false
                        }
                    )
                    mexicoLocations.forEach { mexicoState ->
                        DropdownMenuItem(
                            text = { Text(mexicoState.state) },
                            onClick = {
                                local = local.copy(state = mexicoState.state, city = "")
                                stateExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // City Selection
            ExposedDropdownMenuBox(
                expanded = cityExpanded,
                onExpandedChange = { if (local.state.isNotEmpty()) cityExpanded = !cityExpanded }
            ) {
                OutlinedTextField(
                    value = local.city,
                    onValueChange = {},
                    readOnly = true,
                    enabled = local.state.isNotEmpty(),
                    label = { Text("Ciudad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0D3B4F),
                        focusedLabelColor = Color(0xFF0D3B4F)
                    )
                )
                if (municipalities.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = cityExpanded,
                        onDismissRequest = { cityExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas") },
                            onClick = {
                                local = local.copy(city = "")
                                cityExpanded = false
                            }
                        )
                        municipalities.forEach { city ->
                            DropdownMenuItem(
                                text = { Text(city) },
                                onClick = {
                                    local = local.copy(city = city)
                                    cityExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            val minDisplay = "$${"%,.0f".format(priceRange.start)}"
            val maxDisplay = if (priceRange.endInclusive >= safeMax * 0.99f) "Máx"
                             else "$${"%,.0f".format(priceRange.endInclusive)}"
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FilterSectionTitle("Rango de precio")
                Text("$minDisplay – $maxDisplay", fontSize = 13.sp, color = Color.Gray)
            }
            RangeSlider(
                value = priceRange,
                onValueChange = { priceRange = it },
                valueRange = 0f..safeMax,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF0D3B4F),
                    activeTrackColor = Color(0xFF0D3B4F)
                )
            )
            Spacer(Modifier.height(16.dp))

            FilterSectionTitle("Características mínimas")
            Spacer(Modifier.height(8.dp))
            StepCounterRow("Habitaciones", local.minRooms,
                onInc = { local = local.copy(minRooms = local.minRooms + 1) },
                onDec = { if (local.minRooms > 0) local = local.copy(minRooms = local.minRooms - 1) }
            )
            StepCounterRow("Baños", local.minBathrooms,
                onInc = { local = local.copy(minBathrooms = local.minBathrooms + 1) },
                onDec = { if (local.minBathrooms > 0) local = local.copy(minBathrooms = local.minBathrooms - 1) }
            )
            StepCounterRow("Estacionamientos", local.minParking,
                onInc = { local = local.copy(minParking = local.minParking + 1) },
                onDec = { if (local.minParking > 0) local = local.copy(minParking = local.minParking - 1) }
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    onApply(local.copy(
                        minPrice = priceRange.start.toDouble(),
                        maxPrice = if (priceRange.endInclusive >= safeMax * 0.99f) Double.MAX_VALUE
                                   else priceRange.endInclusive.toDouble()
                    ))
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3B4F))
            ) {
                Text("Aplicar filtros", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun FilterSectionTitle(title: String) {
    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
}

@Composable
private fun StepCounterRow(label: String, value: Int, onInc: () -> Unit, onDec: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 15.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDec, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Remove, null,
                    tint = if (value > 0) Color(0xFF0D3B4F) else Color.LightGray)
            }
            Text(
                text = "$value",
                modifier = Modifier.widthIn(min = 28.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            IconButton(onClick = onInc, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Add, null, tint = Color(0xFF0D3B4F))
            }
        }
    }
}
