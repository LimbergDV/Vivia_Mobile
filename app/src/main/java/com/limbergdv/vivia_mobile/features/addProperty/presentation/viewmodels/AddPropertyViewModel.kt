package com.limbergdv.vivia_mobile.features.addProperty.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.core.hardware.domain.CameraManager
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.*
import com.limbergdv.vivia_mobile.features.addProperty.domain.usecases.AddPropertyUseCases
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val useCases: AddPropertyUseCases,
    private val cameraManager: CameraManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPropertyUiState())
    val uiState = _uiState.asStateFlow()

    // Guarda el Uri creado para la cámara hasta que la foto sea confirmada
    private var pendingCameraUri: Uri? = null

    init {
        loadDraft()
    }

    // ── Carga del borrador ────────────────────────────────────────────────────

    private fun loadDraft() {
        useCases.getDraft().onEach { savedDraft ->
            if (savedDraft != null && _uiState.value == AddPropertyUiState()) {
                _uiState.value = savedDraft
            }
        }.launchIn(viewModelScope)
    }

    // ── Auto-save ─────────────────────────────────────────────────────────────

    private fun saveDraft() {
        viewModelScope.launch {
            useCases.saveDraft(_uiState.value)
        }
    }

    // ── Paso 1: Información básica ────────────────────────────────────────────

    fun onListingTypeChange(type: ListingType) {
        _uiState.update { it.copy(listingType = type) }
        saveDraft()
    }

    fun onCityChange(value: String) {
        _uiState.update { it.copy(city = value) }
        saveDraft()
    }

    fun onStateChange(value: String) {
        _uiState.update { it.copy(state = value) }
        saveDraft()
    }

    fun onNeighborhoodChange(value: String) {
        _uiState.update { it.copy(neighborhood = value) }
        saveDraft()
    }

    fun onPropertyTypeChange(type: PropertyType) {
        _uiState.update { it.copy(propertyType = type) }
        saveDraft()
    }

    fun onPriceChange(value: String) {
        _uiState.update { it.copy(price = value) }
        saveDraft()
    }

    fun onLandAreaChange(value: String) {
        _uiState.update { it.copy(landArea = value) }
        saveDraft()
    }

    // ── Paso 2: Detalles ──────────────────────────────────────────────────────

    fun onBedroomsChange(value: Int?) {
        _uiState.update { it.copy(bedrooms = value) }
        saveDraft()
    }

    fun onBathroomsChange(value: Int?) {
        _uiState.update { it.copy(bathrooms = value) }
        saveDraft()
    }

    fun onParkingSpacesChange(value: Int?) {
        _uiState.update { it.copy(parkingSpaces = value) }
        saveDraft()
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
        saveDraft()
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
        saveDraft()
    }

    // ── Paso 3: Imágenes ──────────────────────────────────────────────────────

    fun onImagesSelected(uris: List<Uri>) {
        _uiState.update { current ->
            current.copy(selectedImages = current.selectedImages + uris)
        }
        saveDraft()
    }

    fun onRemoveImage(uri: Uri) {
        _uiState.update { current ->
            current.copy(selectedImages = current.selectedImages.filter { it != uri })
        }
        saveDraft()
    }

    // ── Cámara ────────────────────────────────────────────────────────────────

    fun prepareCameraUri(): Uri? {
        val uri = cameraManager.createPhotoUri()
        pendingCameraUri = uri
        return uri
    }

    fun onPhotoCaptured(success: Boolean) {
        if (success) {
            val uri = pendingCameraUri ?: return
            pendingCameraUri = null
            onImagesSelected(listOf(uri))
        } else {
            pendingCameraUri = null
        }
    }

    // ── Navegación de pasos ───────────────────────────────────────────────────

    fun onNextStep() {
        val current = _uiState.value.currentStep
        if (current < 3) {
            _uiState.update { it.copy(currentStep = current + 1) }
            saveDraft()
        }
    }

    fun onPreviousStep() {
        val current = _uiState.value.currentStep
        if (current > 1) {
            _uiState.update { it.copy(currentStep = current - 1) }
            saveDraft()
        }
    }

    // ── Aliases que usa AddPropertyScreen ─────────────────────────────────────

    fun onBack() = onPreviousStep()
    fun onNextFromStep1() = onNextStep()
    fun onNextFromStep2() = onNextStep()

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ── Submit ────────────────────────────────────────────────────────────────

    fun onSubmit() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val property = Property(
                    listingType   = state.listingType,
                    city          = state.city,
                    state         = state.state,
                    neighborhood  = state.neighborhood,
                    propertyType  = state.propertyType,
                    price         = state.price.toDoubleOrNull() ?: 0.0,
                    landArea      = state.landArea.toDoubleOrNull() ?: 0.0,
                    bedrooms      = state.bedrooms ?: 0,
                    bathrooms     = state.bathrooms ?: 0,
                    parkingSpaces = state.parkingSpaces ?: 0,
                    title         = state.title,
                    description   = state.description,
                    imageUris     = state.selectedImages.map { it.toString() }
                )

                val result = useCases.createProperty(property)

                result.fold(
                    onSuccess = {
                        useCases.clearDraft()
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isLoading = false, error = error.message) }
                    }
                )

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}