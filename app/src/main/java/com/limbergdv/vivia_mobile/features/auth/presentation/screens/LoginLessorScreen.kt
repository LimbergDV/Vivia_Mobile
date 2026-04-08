package com.limbergdv.vivia_mobile.features.auth.presentation.screens

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.R
import com.limbergdv.vivia_mobile.features.auth.presentation.components.DividerWithText
import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.AuthUiState
import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.LoginLessorEvent
import com.limbergdv.vivia_mobile.features.auth.presentation.viewmodels.LoginLessorViewModel
import com.limbergdv.vivia_mobile.features.home.presentation.components.BrandHeader
import com.limbergdv.vivia_mobile.features.home.presentation.components.PrimaryButton
import com.limbergdv.vivia_mobile.features.users.lessors.presentation.components.ViviaTextField

@Composable
fun LoginLessorScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateNext: () -> Unit,
    viewModel: LoginLessorViewModel = hiltViewModel()
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
                viewModel.onEvent(LoginLessorEvent.ResetUiState)
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
            Spacer(modifier = Modifier.height(64.dp))
            BrandHeader()
            Spacer(modifier = Modifier.height(48.dp))

            ViviaTextField(
                label = "Nombre de la empresa",
                placeholder = "Ingresa el nombre de la empresa",
                value = state.companyName,
                onValueChange = { viewModel.onEvent(LoginLessorEvent.CompanyNameChanged(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViviaTextField(
                label = "Contraseña",
                placeholder = "Ingresa tu contraseña",
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginLessorEvent.PasswordChanged(it)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Iniciar Sesión",
                onClick = {
                    keyboardController?.hide()
                    viewModel.onEvent(LoginLessorEvent.TraditionalLoginClicked)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is AuthUiState.Loading
            )

            Spacer(modifier = Modifier.height(32.dp))
            DividerWithText(text = "ó")
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Usa tu huella dactilar",
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable(enabled = uiState !is AuthUiState.Loading) {
                        keyboardController?.hide()
                        viewModel.onBiometricLogin(context)
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
            
            Row(
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .clickable { onNavigateToRegister() },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "¿No tienes cuenta? ", style = TextStyle(fontSize = 16.sp, color = Color.Black))
                Text(
                    text = "Crea Una",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, textDecoration = TextDecoration.Underline)
                )
            }
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
