package com.limbergdv.vivia_mobile.features.users.lessees.presentation.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.R
import com.limbergdv.vivia_mobile.features.home.presentation.components.SecondaryTextButton
import com.limbergdv.vivia_mobile.features.users.lessors.components.ViviaTextField


@Composable
fun RegisterLesseeScreen(
    onCancelClick: () -> Unit,
    onFingerprintClick: () -> Unit // Parámetro para conectar la lógica biométrica después
) {
    // Estados para los valores del formulario
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()) // Permite desplazar la pantalla
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Asumiendo que tienes un XML solo del icono de la casa
        Image(
            painter = painterResource(id = R.drawable.ic_vivia_logo_only),
            contentDescription = "Logo Vívia",
            modifier = Modifier.height(80.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Por favor, llena el siguiente formulario",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        ViviaTextField(
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            value = name,
            onValueChange = { name = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ViviaTextField(
            label = "Correo Electrónico",
            placeholder = "Email",
            value = lastName,
            onValueChange = { lastName = it }
        )



        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Por favor, ingrese su huella dactilar, funcionará como contraseña",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                lineHeight = 24.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Espacio reservado para la huella dactilar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable { onFingerprintClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Reemplaza con tu XML de huella
                contentDescription = "Sensor de huella dactilar",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        SecondaryTextButton(
            text = "Cancelar",
            onClick = onCancelClick
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterLesseeScreenPreview() {
    RegisterLesseeScreen(
        onCancelClick = {},
        onFingerprintClick = {}
    )
}