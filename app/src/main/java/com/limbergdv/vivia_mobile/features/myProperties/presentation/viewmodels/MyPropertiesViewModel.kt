package com.limbergdv.vivia_mobile.features.myProperties.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.GetMyPropertiesUseCase
import com.limbergdv.vivia_mobile.features.myProperties.presentation.screens.MyPropertiesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MyPropertiesViewModel @Inject constructor(
    private val getMyProperties: GetMyPropertiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPropertiesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        getMyProperties()
            .onStart {
                _uiState.update { it.copy(isLoading = true) }
            }
            .onEach { properties ->
                _uiState.update { it.copy(isLoading = false, properties = properties) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}