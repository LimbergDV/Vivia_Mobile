package com.limbergdv.vivia_mobile.features.addProperty.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.ListingType
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.Property
import com.limbergdv.vivia_mobile.features.addProperty.domain.entities.PropertyType
import com.limbergdv.vivia_mobile.features.addProperty.domain.usecases.CreatePropertyUseCase
import com.limbergdv.vivia_mobile.features.addProperty.presentation.screens.AddPropertyUiState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val createPropertyUseCase: CreatePropertyUseCase
    // TODO: Inyectar AppNavigator cuando la navegación esté configurada
    // private val navigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPropertyUiState())
    val uiState = _uiState.asStateFlow()

    // ── Paso 1: Setters ─────────────────────────────────────────────────────

    fun onListingTypeChange(type: ListingType) {
        _uiState.update { it.copy(listingType = type, price = "") }
    }

    fun onCityChange(value: String) {
        _uiState.update { it.copy(city = value) }
    }

    fun onStateChange(value: String) {
        _uiState.update { it.copy(state = value) }
    }

    fun onNeighborhoodChange(value: String) {
        _uiState.update { it.copy(neighborhood = value) }
    }

    fun onPropertyTypeChange(type: PropertyType) {
        _uiState.update { it.copy(propertyType = type) }
    }

    fun onPriceChange(value: String) {
        if (value.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(price = value) }
        }
    }

    fun onLandAreaChange(value: String) {
        if (value.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(landArea = value) }
        }
    }

    /** Valida el paso 1 y avanza al paso 2 */
    fun onNextFromStep1() {
        val state = _uiState.value
        val priceValue = state.price.toDoubleOrNull()
        val areaValue  = state.landArea.toDoubleOrNull()

        when {
            state.city.isBlank() || state.state.isBlank() -> {
                _uiState.update { it.copy(error = "Ciudad y estado son obligatorios") }
            }
            priceValue == null || priceValue <= 0 -> {
                _uiState.update { it.copy(error = "Ingresa un precio válido") }
            }
            areaValue == null || areaValue <= 0 -> {
                _uiState.update { it.copy(error = "Ingresa el área del terreno") }
            }
            else -> _uiState.update { it.copy(currentStep = 2, error = null) }
        }
    }

    // ── Paso 2: Setters ─────────────────────────────────────────────────────

    fun onBedroomsChange(value: Int) {
        _uiState.update { it.copy(bedrooms = value) }
    }

    fun onBathroomsChange(value: Int) {
        _uiState.update { it.copy(bathrooms = value) }
    }

    fun onParkingSpacesChange(value: Int) {
        _uiState.update { it.copy(parkingSpaces = value) }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    /** Valida el paso 2 y avanza al paso 3 */
    fun onNextFromStep2() {
        val state = _uiState.value
        when {
            state.bedrooms == null -> {
                _uiState.update { it.copy(error = "Selecciona el número de habitaciones") }
            }
            state.bathrooms == null -> {
                _uiState.update { it.copy(error = "Selecciona el número de baños") }
            }
            state.title.isBlank() -> {
                _uiState.update { it.copy(error = "El título es obligatorio") }
            }
            else -> _uiState.update { it.copy(currentStep = 3, error = null) }
        }
    }

    /** Vuelve al paso anterior */
    fun onBack() {
        val current = _uiState.value.currentStep
        if (current > 1) {
            _uiState.update { it.copy(currentStep = current - 1, error = null) }
        } else {
            // TODO: navegar hacia atrás con el navigator cuando esté disponible
            // navigator.popBackStack()
        }
    }

    // ── Paso 3: Imágenes ────────────────────────────────────────────────────

    fun onImagesSelected(uris: List<Uri>) {
        _uiState.update { state ->
            val combined = (state.selectedImages + uris).distinct().take(10) // máx 10 imágenes
            state.copy(selectedImages = combined)
        }
    }

    fun onRemoveImage(uri: Uri) {
        _uiState.update { state ->
            state.copy(selectedImages = state.selectedImages.filter { it != uri })
        }
    }

    // ── Envío final ─────────────────────────────────────────────────────────

    fun onSubmit() {
        val state = _uiState.value

        val property = Property(
            listingType   = state.listingType,
            city          = state.city,
            state         = state.state,
            neighborhood  = state.neighborhood,
            propertyType  = state.propertyType,
            price         = state.price.toDoubleOrNull() ?: 0.0,
            landArea      = state.landArea.toDoubleOrNull() ?: 0.0,
            bedrooms      = state.bedrooms ?: 1,
            bathrooms     = state.bathrooms ?: 1,
            parkingSpaces = state.parkingSpaces ?: 0,
            title         = state.title,
            description   = state.description,
            imageUris     = state.selectedImages.map { it.toString() }
        )

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = createPropertyUseCase(property)
            _uiState.update { current ->
                result.fold(
                    onSuccess = {
                        current.copy(isLoading = false, isSuccess = true)
                    },
                    onFailure = { e ->
                        current.copy(isLoading = false, error = e.message ?: "Error al publicar")
                    }
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}