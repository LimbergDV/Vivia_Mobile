package com.limbergdv.vivia_mobile.features.home.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.core.theme.AppTheme
import com.limbergdv.vivia_mobile.features.home.presentation.components.BrandHeader
import com.limbergdv.vivia_mobile.features.home.presentation.components.OptionsCard
import com.limbergdv.vivia_mobile.features.home.presentation.components.SecondaryTextButton

@Composable
fun LessorOptionScreen(
    onNavigateBack: () -> Unit,
    toLoginLessor: () -> Unit,
    toRegisterLessor: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(128.dp)) // Margen superior

        BrandHeader()

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "¡Nos da gusto que quieras ser parte de nuestra comunidad!",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black,
                lineHeight = 25.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OptionsCard(
            onLoginClick = toLoginLessor,
            onRegisterClick = toRegisterLessor,
            modifier = Modifier.weight(3f, fill = false) // Evita que empuje elementos fuera de pantalla
        )

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón "Volver" hacia abajo

        SecondaryTextButton(
            text = "Volver",
            onClick = onNavigateBack
        )

        Spacer(modifier = Modifier.height(48.dp)) // Margen inferior
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Pantalla de Inicio Default"
)
@Composable
fun LessorOptionScreenPreview() {
    // Es buena práctica envolver el preview en el tema de tu aplicación
    AppTheme {
        LessorOptionScreen ({},{},{})
    }
}