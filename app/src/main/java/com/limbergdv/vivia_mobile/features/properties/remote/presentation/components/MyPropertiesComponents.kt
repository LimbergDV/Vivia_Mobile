package com.limbergdv.vivia_mobile.features.properties.remote.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.limbergdv.vivia_mobile.R
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.Property

val ViviaNavy  = Color(0xFF0D3B4F)
val ViviaBlue  = Color(0xFF1A3A6B)
val ViviaGray  = Color(0xFFF5F5F5)
val ViviaLight = Color(0xFFEEF2F7)

// ── PropertyCard ─────────────────────────────────────────────────────────────
@Composable
fun PropertyCard(
    property: Property,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // ── Imagen ───────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(ViviaLight)
            ) {
                if (property.imageUris.isNotEmpty()) {
                    AsyncImage(
                        model = property.imageUris.first(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_vivia_logo_only),
                        error = painterResource(R.drawable.ic_vivia_logo_only)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        tint = ViviaNavy.copy(alpha = 0.3f)
                    )
                }

                // Badge Venta / Renta
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (property.listingType == ListingType.VENTA)
                                ViviaNavy else Color(0xFF2E7D32)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = property.listingType.name,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ── Info ─────────────────────────────────────────────────────────
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = property.propertyType.label,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = "$${"%,.0f".format(property.price)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ViviaBlue
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PropertyChip(
                        icon = Icons.Outlined.SquareFoot,
                        label = "${property.landArea.toInt()}m2"
                    )
                    PropertyChip(
                        icon = Icons.Outlined.Bed,
                        label = "${property.bedrooms}"
                    )
                    PropertyChip(
                        icon = Icons.Outlined.Bathtub,
                        label = "${property.bathrooms}"
                    )
                }
            }
        }
    }
}

// ── Chip pequeño con icono ────────────────────────────────────────────────────
@Composable
fun PropertyChip(
    icon: ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = ViviaBlue
        )
        Text(text = label, fontSize = 12.sp, color = Color.DarkGray)
    }
}

// ── Vivia Bottom Navigation Bar ───────────────────────────────────────────────
@Composable
fun ViviaBottomBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddClick: () -> Unit,
    onFollowersClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                icon = Icons.Outlined.Home,
                isSelected = currentRoute == "myProperties",
                onClick = onHomeClick
            )
            NavBarItem(
                icon = Icons.Outlined.Person,
                isSelected = currentRoute == "profile",
                onClick = onProfileClick
            )

            // Botón central +
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(ViviaNavy)
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            NavBarItem(
                icon = Icons.Outlined.Group,
                isSelected = currentRoute == "followers",
                onClick = onFollowersClick
            )
            NavBarItem(
                icon = Icons.Outlined.ExitToApp,
                isSelected = currentRoute == "logout",
                onClick = onLogoutClick
            )
        }
    }
}

// ── Ítem individual de la navbar ──────────────────────────────────────────────
@Composable
private fun NavBarItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ViviaLight else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) ViviaNavy else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}