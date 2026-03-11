package com.limbergdv.vivia_mobile.features.users.lessees.presentation.screens

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
import com.limbergdv.vivia_mobile.core.hardware.data.BiometricServiceImpl
import com.limbergdv.vivia_mobile.features.home.presentation.components.SecondaryTextButton
import com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels.RegisterLesseeEvent
import com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels.RegisterLesseeViewModel
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.components.ViviaTextField

@Composable
fun RegisterLesseeScreen(
    onCancelClick: () -> Unit,
    onNavigateNext: () -> Unit,
    viewModel: RegisterLesseeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.webAuthnChallenge) {
        state.webAuthnChallenge?.let { challenge ->
            val credentialManager = CredentialManager.create(context)
            val biometricService = BiometricServiceImpl(credentialManager)

            val result = biometricService.registerBiometric(context, challenge)

            result.fold(
                onSuccess = { credentialJson ->
                    viewModel.onEvent(RegisterLesseeEvent.OnBiometricSuccess(credentialJson))
                },
                onFailure = { error ->
                    viewModel.onEvent(RegisterLesseeEvent.OnBiometricError(error.message ?: "Cancelado"))
                }
            )
            viewModel.onEvent(RegisterLesseeEvent.ConsumeChallenge)
        }
    }

    LaunchedEffect(state.isRegistrationSuccessful) {
        if (state.isRegistrationSuccessful) {
            onNavigateNext()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onEvent(RegisterLesseeEvent.ConsumeError)
        }
    }

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
                text = "Por favor, llena el siguiente formulario para Arrendatario",
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            ViviaTextField(
                label = "Nombre de usuario",
                placeholder = "Escribe tu nombre de usuario",
                value = state.username,
                onValueChange = { viewModel.onEvent(RegisterLesseeEvent.UsernameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ViviaTextField(
                label = "Correo Electrónico",
                placeholder = "Email",
                value = state.email,
                onValueChange = { viewModel.onEvent(RegisterLesseeEvent.EmailChanged(it)) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Por favor, ingrese su huella dactilar, funcionará como contraseña",
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.Black, lineHeight = 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (!state.isLoading) {
                            viewModel.onEvent(RegisterLesseeEvent.RegisterClicked)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_huella_image),
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

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}