package com.limbergdv.vivia_mobile.features.users.lessors.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.R
import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.AuthUiState
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
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                onNavigateNext()
            }
            is AuthUiState.Error -> {
                Toast.makeText(context, (uiState as AuthUiState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.onEvent(RegisterLessorEvent.ResetUiState)
            }
            else -> {}
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
                text = "Por favor, llena el siguiente formulario para Arrendador",
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            ViviaTextField(
                label = "Nombre (s)",
                placeholder = "Escribe tu nombre",
                value = state.firstName,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.FirstNameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViviaTextField(
                label = "Apellidos",
                placeholder = "Escribe tus apellidos",
                value = state.lastName,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.LastNameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViviaTextField(
                label = "Nombre de la empresa",
                placeholder = "Escribe un nombre por el cuál publicar",
                value = state.companyName,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.CompanyNameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViviaTextField(
                label = "Teléfono",
                placeholder = "Escribe tu número telefónico",
                value = state.phoneNumber,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.PhoneNumberChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViviaTextField(
                label = "Contraseña",
                placeholder = "Ingresa tu contraseña",
                value = state.password,
                onValueChange = { viewModel.onEvent(RegisterLessorEvent.PasswordChanged(it)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Por favor, ingrese su huella dactilar, funcionará como contraseña biométrica",
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.Black, lineHeight = 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable(enabled = uiState !is AuthUiState.Loading) {
                        keyboardController?.hide()
                        viewModel.onRegister(context)
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

        if (uiState is AuthUiState.Loading) {
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
