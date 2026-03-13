package com.limbergdv.vivia_mobile.features.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.limbergdv.vivia_mobile.R

@Composable
fun BrandLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_vivia_logo_title), // Reemplazar con tu XML del logo
        contentDescription = "Logo Vívia",
        modifier = modifier.height(80.dp)
    )
}