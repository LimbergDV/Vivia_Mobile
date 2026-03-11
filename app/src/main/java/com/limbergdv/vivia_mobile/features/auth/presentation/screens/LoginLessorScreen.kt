package com.limbergdv.vivia_mobile.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.features.auth.presentation.components.DividerWithText
import com.limbergdv.vivia_mobile.features.home.presentation.components.BrandHeader
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.components.ViviaTextField

@Composable
fun LoginLessorScreen(
    onNavigateToRegister: () -> Unit,
    onFingerprintClick: () -> Unit,
    onNavigateNext: () -> Unit
) {
    var companyName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Componente reutilizado
        BrandHeader()

        Spacer(modifier = Modifier.height(4.dp))

        // Componente reutilizado
        ViviaTextField(
            label = "Nombre de la empresa",
            placeholder = "Ingresa el nombre de la empresa",
            value = companyName,
            onValueChange = { companyName = it }
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Por favor, ingrese su huella dactilar",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Espacio en blanco reservado para la huella dactilar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable { onFingerprintClick() },
            contentAlignment = Alignment.Center
        ) {
            // Se deja en blanco según las instrucciones
        }

        Spacer(modifier = Modifier.height(48.dp))

        DividerWithText(text = "ó")

        Spacer(modifier = Modifier.height(32.dp))

        // Texto inferior clickeable para ir al registro
        Row(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .clickable { onNavigateToRegister() },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes cuenta? ",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )
            Text(
                text = "Crea Una",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginLessorScreenPreview() {
    LoginLessorScreen(
        onNavigateToRegister = {},
        onFingerprintClick = {},
        onNavigateNext = {}
    )
}