package com.limbergdv.vivia_mobile.features.myProperties.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.myProperties.presentation.components.ViviaBlue
import com.limbergdv.vivia_mobile.features.myProperties.presentation.components.ViviaLight
import com.limbergdv.vivia_mobile.features.myProperties.presentation.components.ViviaNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailScreen(
    property: Property,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // ── Hero: imagen full-width ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                if (property.imageUris.isNotEmpty()) {
                    AsyncImage(
                        model = property.imageUris.first(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ViviaLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = ViviaNavy.copy(alpha = 0.3f)
                        )
                    }
                }

                // Botón atrás sobre la imagen
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                }

                // Indicadores de imagen (dots)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(maxOf(property.imageUris.size, 1).coerceAtMost(5)) { index ->
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (index == 0) 24.dp else 12.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == 0) ViviaNavy
                                    else Color.White.copy(alpha = 0.6f)
                                )
                        )
                    }
                }
            }

            // ── Contenido ────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F6FA))
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Título y precio ───────────────────────────────────────────
                Text(
                    text = property.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "$${"%,.0f".format(property.price)}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = ViviaBlue
                )

                // ── Grid de características ───────────────────────────────────
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            DetailItem(
                                icon  = Icons.Outlined.LocationOn,
                                label = "Ubicación",
                                value = "${property.city}, ${property.state}",
                                modifier = Modifier.weight(1f)
                            )
                            DetailItem(
                                icon  = Icons.Outlined.SquareFoot,
                                label = "Área",
                                value = "${property.landArea.toInt()} m2",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DetailItem(
                                icon  = Icons.Outlined.Bed,
                                label = "Habitaciones",
                                value = "${property.bedrooms} habitaciones",
                                modifier = Modifier.weight(1f)
                            )
                            DetailItem(
                                icon  = Icons.Outlined.Bathtub,
                                label = "Baños",
                                value = "${property.bathrooms} baños",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DetailItem(
                                icon  = Icons.Outlined.DirectionsCar,
                                label = "Estacionamiento",
                                value = "${property.parkingSpaces} espacios",
                                modifier = Modifier.weight(1f)
                            )
                            DetailItem(
                                icon  = Icons.Outlined.Home,
                                label = "Tipo",
                                value = property.propertyType.label,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DetailItem(
                                icon  = Icons.Outlined.Sell,
                                label = "Modalidad",
                                value = if (property.listingType == ListingType.VENTA)
                                    "En venta" else "En renta",
                                modifier = Modifier.weight(1f)
                            )
                            DetailItem(
                                icon  = Icons.Outlined.LocationOn,
                                label = "Colonia",
                                value = property.neighborhood,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // ── Descripción ───────────────────────────────────────────────
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Descripción",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = property.description,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Item individual de característica ────────────────────────────────────────
@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ViviaLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ViviaBlue,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            Text(text = label, fontSize = 11.sp, color = Color.Gray)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
    }
}