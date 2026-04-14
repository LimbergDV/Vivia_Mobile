package com.limbergdv.vivia_mobile.features.properties.local.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.core.hardware.domain.CameraManager
import com.limbergdv.vivia_mobile.core.network.MexicoLocationManager
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.local.domain.entities.PropertyType
import com.limbergdv.vivia_mobile.features.properties.local.domain.usecases.AddPropertyUseCases
import com.limbergdv.vivia_mobile.features.properties.local.presentation.screens.AddPropertyUiState
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.EnqueueImageUploadUseCase
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
    private val cameraManager: CameraManager,
    private val locationManager: MexicoLocationManager,
    private val enqueueImageUploadUseCase: EnqueueImageUploadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPropertyUiState())
    val uiState = _uiState.asStateFlow()

    private var pendingCameraUri: Uri? = null

    init {
        loadMexicoLocations()
        loadDraft()
    }

    private fun loadMexicoLocations() {
        val locations = locationManager.getMexicoLocations()
        val states = locations.map { it.state }.sorted()
        _uiState.update { it.copy(availableStates = states) }
    }

    // ── Borrador ──────────────────────────────────────────────────────────────

    private fun loadDraft() {
        useCases.getDraft().onEach { savedDraft ->
            if (savedDraft != null && _uiState.value.state.isEmpty()) { // Solo cargar si no hay datos actuales
                _uiState.update { current ->
                    savedDraft.copy(
                        availableStates = current.availableStates,
                        availableMunicipalities = locationManager.getMexicoLocations()
                            .find { it.state == savedDraft.state }?.municipalities?.sorted() ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveDraft() {
        viewModelScope.launch {
            useCases.saveDraft(_uiState.value)
        }
    }

    // ── Paso 1 ────────────────────────────────────────────────────────────────

    fun onListingTypeChange(type: ListingType) {
        _uiState.update { it.copy(listingType = type) }
        saveDraft()
    }

    fun onCityChange(value: String) {
        _uiState.update { it.copy(city = value) }
        saveDraft()
    }

    fun onStateChange(value: String) {
        val municipalities = locationManager.getMexicoLocations()
            .find { it.state == value }?.municipalities?.sorted() ?: emptyList()
            
        _uiState.update { 
            it.copy(
                state = value,
                availableMunicipalities = municipalities,
                city = "" // Reset city when state changes
            ) 
        }
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

    // ── Paso 2 ────────────────────────────────────────────────────────────────

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

    // ── Paso 3 ────────────────────────────────────────────────────────────────

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

        // Log para ver el estado completo antes de enviar
        Log.d("VIVIA_PROPERTY_DEBUG", "=== UiState al hacer submit ===")
        Log.d("VIVIA_PROPERTY_DEBUG", "city: '${state.city}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "state: '${state.state}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "neighborhood: '${state.neighborhood}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "price: '${state.price}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "landArea: '${state.landArea}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "title: '${state.title}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "description: '${state.description}'")
        Log.d("VIVIA_PROPERTY_DEBUG", "bedrooms: ${state.bedrooms}")
        Log.d("VIVIA_PROPERTY_DEBUG", "bathrooms: ${state.bathrooms}")
        Log.d("VIVIA_PROPERTY_DEBUG", "parkingSpaces: ${state.parkingSpaces}")
        Log.d("VIVIA_PROPERTY_DEBUG", "currentStep: ${state.currentStep}")

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
                    onSuccess = { createdProperty ->
                        // Encolar subida de imágenes en segundo plano
                        if (state.selectedImages.isNotEmpty()) {
                            enqueueImageUploadUseCase(createdProperty.id, state.selectedImages)
                        }
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