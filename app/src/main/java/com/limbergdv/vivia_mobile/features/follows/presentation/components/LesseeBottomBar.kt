package com.limbergdv.vivia_mobile.features.follows.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val ViviaNavy  = Color(0xFF0D3B4F)
val ViviaLight = Color(0xFFEEF2F7)

@Composable
fun LesseeBottomBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onFollowsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
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
                isSelected = currentRoute == "home",
                onClick = onHomeClick
            )
            NavBarItem(
                icon = Icons.Outlined.Group,
                isSelected = currentRoute == "follows",
                onClick = onFollowsClick
            )
            NavBarItem(
                icon = Icons.Outlined.Search,
                isSelected = currentRoute == "search",
                onClick = onSearchClick
            )
            NavBarItem(
                icon = Icons.Outlined.Person,
                isSelected = currentRoute == "profile",
                onClick = onProfileClick
            )
            NavBarItem(
                icon = Icons.Outlined.ExitToApp,
                isSelected = false,
                onClick = onLogoutClick
            )
        }
    }
}

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
