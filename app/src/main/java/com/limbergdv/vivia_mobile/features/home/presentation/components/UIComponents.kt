package com.limbergdv.vivia_mobile.features.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.limbergdv.vivia_mobile.R


@Composable
fun BrandHeader(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_vivia_logo_phrase), // Tu archivo XML
            contentDescription = "Logo Vívia",
            modifier = Modifier.height(300.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        /*Text(
            text = "Buscar casa sin tanto drama.",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xFF444444)
            )
        )*/
    }
}

@Composable
fun OptionsCard(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F2FA), RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Elige una de las opciones:",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Entrar",
                onClick = onLoginClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Registrarme",
                onClick = onRegisterClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crea una cuenta y comienza a publicar tus propiedades para vender o rentarlas",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF444444),
                    lineHeight = 16.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}