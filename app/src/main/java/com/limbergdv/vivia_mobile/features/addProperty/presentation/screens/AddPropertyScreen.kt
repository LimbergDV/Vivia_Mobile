package com.limbergdv.vivia_mobile.features.addProperty.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.ViviaNavy
import com.limbergdv.vivia_mobile.features.addProperty.presentation.viewmodels.AddPropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(
    viewModel: AddPropertyViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { msg ->
            snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.clearSuccess()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agregar Una Propiedad",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    if (uiState.currentStep > 1) {
                        IconButton(onClick = { viewModel.onBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFFB00020),
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentStep) {
                1 -> AddPropertyStep1Screen(
                    uiState              = uiState,
                    onListingTypeChange  = viewModel::onListingTypeChange,
                    onCityChange         = viewModel::onCityChange,
                    onStateChange        = viewModel::onStateChange,
                    onNeighborhoodChange = viewModel::onNeighborhoodChange,
                    onPropertyTypeChange = viewModel::onPropertyTypeChange,
                    onPriceChange        = viewModel::onPriceChange,
                    onLandAreaChange     = viewModel::onLandAreaChange,
                    onNext               = viewModel::onNextFromStep1,
                    onError              = {}
                )
                2 -> AddPropertyStep2Screen(
                    uiState               = uiState,
                    onBedroomsChange      = viewModel::onBedroomsChange,
                    onBathroomsChange     = viewModel::onBathroomsChange,
                    onParkingSpacesChange = viewModel::onParkingSpacesChange,
                    onTitleChange         = viewModel::onTitleChange,
                    onDescriptionChange   = viewModel::onDescriptionChange,
                    onNext                = viewModel::onNextFromStep2,
                    onBack                = viewModel::onBack
                )
                3 -> AddPropertyStep3Screen(
                    uiState            = uiState,
                    onImagesSelected   = viewModel::onImagesSelected,
                    onRemoveImage      = viewModel::onRemoveImage,
                    onSubmit           = viewModel::onSubmit,
                    onBack             = viewModel::onBack,
                    onPrepareCameraUri = viewModel::prepareCameraUri,
                    onPhotoCaptured    = viewModel::onPhotoCaptured
                )
            }
        }
    }
}