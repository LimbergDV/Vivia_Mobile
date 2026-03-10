package com.limbergdv.vivia_mobile.features.home.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.limbergdv.vivia_mobile.core.theme.AppTheme
import com.limbergdv.vivia_mobile.features.home.presentation.components.BrandLogo
import com.limbergdv.vivia_mobile.features.home.presentation.components.HeroImage
import com.limbergdv.vivia_mobile.features.home.presentation.components.PrimaryButton
import com.limbergdv.vivia_mobile.features.home.presentation.components.SecondaryTextButton


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espacio para la barra de estado (Status Bar)
        Spacer(modifier = Modifier.height(48.dp))

        HeroImage(
            modifier = Modifier
                .weight(1f) // Toma el espacio disponible dinámicamente
                .padding(vertical = 16.dp)
        )

        BrandLogo()

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Buscar un hogar",
            onClick = { /* Acción de buscar */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryTextButton(
            text = "Unirme como arrendador",
            onClick = { /* Acción de unirse */ }
        )

        // Espacio para la barra de navegación del sistema
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla de Inicio Default"
)
@Composable
fun HomeScreenPreview() {
    // Es buena práctica envolver el preview en el tema de tu aplicación
    AppTheme {
    HomeScreen()
    }
}