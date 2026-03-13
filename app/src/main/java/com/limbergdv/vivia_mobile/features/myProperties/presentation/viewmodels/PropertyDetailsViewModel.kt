package com.limbergdv.vivia_mobile.features.myProperties.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.GetPropertyDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PropertyDetailsUiState())
    val uiState: StateFlow<PropertyDetailsUiState> = _uiState.asStateFlow()

    init {
        val propertyId: String? = savedStateHandle["propertyId"]
        if (propertyId != null) {
            observePropertyDetails(propertyId)
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
}
