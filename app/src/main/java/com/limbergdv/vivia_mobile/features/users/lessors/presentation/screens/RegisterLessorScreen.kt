package com.limbergdv.vivia_mobile.features.users.lessors.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.R
import com.limbergdv.vivia_mobile.core.hardware.data.BiometricServiceImpl // Asegúrate de la importación correcta
import com.limbergdv.vivia_mobile.features.home.presentation.components.SecondaryTextButton
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.components.ViviaTextField
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels.RegisterLessorEvent
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels.RegisterLessorViewModel

@Composable
fun RegisterLessorScreen(
    onCancelClick: () -> Unit,
    onNavigateNext: () -> Unit,
    viewModel: RegisterLessorViewModel = hiltViewModel()
) {
    // 1. Colectar el estado con el ciclo de vida seguro
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 2. Efecto para lanzar el hardware biométrico cuando llegue el desafío
    LaunchedEffect(state.webAuthnChallenge) {
        state.webAuthnChallenge?.let { challenge ->
            // Instanciamos el servicio (puede inyectarse también, pero así es directo)
            val credentialManager = CredentialManager.create(context)
            val biometricService = BiometricServiceImpl(credentialManager)

            // Invocamos el hardware
            val result = biometricService.registerBiometric(context, challenge)

            result.fold(
                onSuccess = { credentialJson ->
                    viewModel.onEvent(RegisterLessorEvent.OnBiometricSuccess(credentialJson))
                },
                onFailure = { error ->
                    viewModel.onEvent(RegisterLessorEvent.OnBiometricError(error.message ?: "Cancelado"))
                }
            )
            viewModel.onEvent(RegisterLessorEvent.ConsumeChallenge)
        }
    }

    // Efecto para navegar si el registro fue exitoso
    LaunchedEffect(state.isRegistrationSuccessful) {
        if (state.isRegistrationSuccessful) {
            onNavigateNext()
        }
    }

    // Efecto para mostrar errores en un Toast
    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onEvent(RegisterLessorEvent.ConsumeError)
        }
    }

    // 3. UI Contenedor Principal
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_vivia_logo_only),
                contentDescription = "Logo Vívia",
                modifier = Modifier.height(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Por favor, llena el siguiente formulario",
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // 4. Conectar los campos al ViewModel
            ViviaTextField(
                label = "Nombre (s)",
                placeholder = "Escribe tu nombre",
                value = state.firstName,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.FirstNameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ViviaTextField(
                label = "Apellidos",
                placeholder = "Escribe tus apellidos",
                value = state.lastName,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.LastNameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ViviaTextField(
                label = "Nombre de la empresa",
                placeholder = "Escribe un nombre por el cuál publicar",
                value = state.companyName,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.CompanyNameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Por favor, ingrese su huella dactilar, funcionará como contraseña",
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.Black, lineHeight = 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. El click de la huella desencadena el flujo en el ViewModel
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        // Solo permite clickear si no está cargando
                        if (!state.isLoading) {
                            viewModel.onEvent(RegisterLessorEvent.RegisterClicked)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background), // Tu icono de huella
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

        // 6. Animación de Carga (Loading Overlay)
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(enabled = false) {}, // Bloquea clicks debajo
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}