package com.limbergdv.vivia_mobile.features.addProperty.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ViviaNavy  = Color(0xFF0D3B4F)   // botones principales
val ViviaPink  = Color(0xFFFF1E8C)   // botón Tomar Foto
val ViviaGray  = Color(0xFFF5F5F5)   // fondo de campos

@Composable
fun ListingTypeToggle(
    isVenta: Boolean,
    onVentaClick: () -> Unit,
    onRentaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ViviaGray)
            .padding(4.dp)
    ) {
        ToggleButton(
            text = "Venta",
            selected = isVenta,
            onClick = onVentaClick,
            modifier = Modifier.weight(1f)
        )
        ToggleButton(
            text = "Renta",
            selected = !isVenta,
            onClick = onRentaClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Color.White else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.Black else Color.Gray,
            fontSize = 15.sp
        )
    }
}

// ─── Dropdown genérico ───────────────────────────────────────────────────────
@Composable
fun <T> ViviaDropdown(
    selectedItem: T?,
    items: List<T>,
    label: String,
    itemLabel: (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ViviaGray)
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedItem?.let { itemLabel(it) } ?: label,
                color = if (selectedItem == null) Color.Gray else Color.Black,
                fontSize = 15.sp
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemLabel(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ─── TextField estilo Vivia ──────────────────────────────────────────────────
@Composable
fun ViviaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ViviaGray,
            unfocusedContainerColor = ViviaGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = ViviaNavy
        )
    )
}

// ─── Selector de número (Habitaciones / Baños / Estacionamiento) ─────────────
@Composable
fun NumberSelector(
    options: List<String>,
    selectedValue: Int?,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val rawValues = options.mapIndexed { index, label ->
        if (label.endsWith("+")) {
            // "10+" → usamos el número base como valor
            label.dropLast(1).toIntOrNull() ?: (index + 1)
        } else {
            label.toIntOrNull() ?: (index + 1)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEachIndexed { index, label ->
            val value = rawValues[index]
            val isSelected = selectedValue == value

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) ViviaNavy else ViviaGray)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) ViviaNavy else Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onSelected(value) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) Color.White else Color.DarkGray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// ─── Sección con ícono y título ──────────────────────────────────────────────
@Composable
fun SectionHeader(
    icon: @Composable () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

// ─── Botón principal Vivia ───────────────────────────────────────────────────
@Composable
fun ViviaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ViviaNavy,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}