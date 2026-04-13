package com.limbergdv.vivia_mobile.features.home.presentation.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun HomeScreen(
    toLoginLessee: () -> Unit = {},
    toOptionsLessor: () -> Unit = {}
) {

    // 1. Preparamos el lanzador para solicitar el permiso
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // El usuario aceptó las notificaciones
                // Opcional: Podrías registrar un evento de analítica o mostrar un Toast
            } else {
                // El usuario rechazó las notificaciones
                // Opcional: Podrías mostrar un Snackbar explicando por qué son útiles
            }
        }
    )

    // 2. Ejecutamos la petición solo una vez al componer la pantalla
    LaunchedEffect(key1 = true) {
        // Comprobamos si el dispositivo tiene Android 13 (Tiramisu) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

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
            onClick = { toLoginLessee() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryTextButton(
            text = "Unirme como arrendador",
            onClick = { toOptionsLessor() }
        )

        // Espacio para la barra de navegación del sistema
        Spacer(modifier = Modifier.height(40.dp))
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