package com.limbergdv.vivia_mobile.features.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.limbergdv.vivia_mobile.R

@Composable
fun HeroImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_home_image), // Reemplazar con tu recurso
        contentDescription = "Interior de un hogar",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape) // Corta la imagen en forma circular/ovalada
    )
}