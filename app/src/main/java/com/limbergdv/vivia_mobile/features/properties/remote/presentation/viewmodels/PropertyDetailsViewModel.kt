package com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.DeletePropertyUseCase
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.GetPropertyDetailsUseCase
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.GetPublicPropertyDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
    private val getPublicPropertyDetailsUseCase: GetPublicPropertyDetailsUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PropertyDetailsUiState())
    val uiState: StateFlow<PropertyDetailsUiState> = _uiState.asStateFlow()

    init {
        val propertyId: String? = savedStateHandle["propertyId"]
        val isLessor: Boolean = savedStateHandle["isLessor"] ?: true
        _uiState.update { it.copy(isLessorMode = isLessor) }

        if (propertyId != null) {
            if (isLessor) {
                observePropertyDetails(propertyId)
            } else {
                fetchPublicPropertyDetails(propertyId)
            }
        } else {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun observePropertyDetails(id: String) {
        getPropertyDetailsUseCase(id)
            .onEach { property ->
                _uiState.update { it.copy(property = property, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchPublicPropertyDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = getPublicPropertyDetailsUseCase(id)
            result.fold(
                onSuccess = { property ->
                    _uiState.update { it.copy(property = property, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun deleteProperty() {
        val propertyId = _uiState.value.property?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, error = null) }
            val result = deletePropertyUseCase(propertyId)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleting = false, isDeleted = true) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isDeleting = false, error = e.message) }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
