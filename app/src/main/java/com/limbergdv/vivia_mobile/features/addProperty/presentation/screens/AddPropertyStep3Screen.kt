package com.limbergdv.vivia_mobile.features.addProperty.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.ViviaButton
import com.limbergdv.vivia_mobile.features.addProperty.presentation.components.ViviaPink


@Composable
fun AddPropertyStep3Screen(
    uiState: AddPropertyUiState,
    onImagesSelected: (List<Uri>) -> Unit,
    onRemoveImage: (Uri) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    // Launcher para seleccionar múltiples imágenes de la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris -> if (uris.isNotEmpty()) onImagesSelected(uris) }

    // Launcher para tomar foto con la cámara
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraUri?.let { onImagesSelected(listOf(it)) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ── Zona de drop / explorar ──────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.5.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Color(0xFFF9F9F9))
                .clickable { galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "Explorar archivos desde su\nteléfono",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ── Preview de imágenes seleccionadas ────────────────────────────────
        if (uiState.selectedImages.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.selectedImages) { uri ->
                    ImagePreviewItem(
                        uri = uri,
                        isUploading = uri in uiState.uploadingImages,
                        onRemove = { onRemoveImage(uri) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ── Botón Tomar Foto ─────────────────────────────────────────────────
        ViviaButton(
            text = "📷  Tomar Foto",
            onClick = {
                // TODO: Crear URI temporal con FileProvider y lanzar cámara
                // val photoUri = createImageUri(context)
                // cameraUri = photoUri
                // cameraLauncher.launch(photoUri)
            },
            backgroundColor = ViviaPink
        )

        // ── Botón Agregar Propiedad ──────────────────────────────────────────
        ViviaButton(
            text = "Agregar Propiedad",
            onClick = onSubmit,
            isLoading = uiState.isLoading
        )
    }
}

@Composable
private fun ImagePreviewItem(
    uri: Uri,
    isUploading: Boolean,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isUploading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cargando...",
                    color = Color.White,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Botón eliminar
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(22.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Eliminar imagen",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}