package com.limbergdv.vivia_mobile.features.myProperties.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.ObserveMyPropertiesUseCase
import com.limbergdv.vivia_mobile.features.myProperties.domain.usecases.SyncMyPropertiesUseCase
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
class MyPropertiesViewModel @Inject constructor(
    private val observeMyPropertiesUseCase: ObserveMyPropertiesUseCase,
    private val syncMyPropertiesUseCase: SyncMyPropertiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPropertiesUiState())
    val uiState: StateFlow<MyPropertiesUiState> = _uiState.asStateFlow()

    init {
        observeProperties()
    }

    private fun observeProperties() {
        observeMyPropertiesUseCase()
            .onEach { properties ->
                _uiState.update { it.copy(properties = properties) }
            }
            .launchIn(viewModelScope)
    }

    fun syncProperties(companyName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, errorMessage = null) }
            val result = syncMyPropertiesUseCase(companyName)
            
            result.onSuccess {
                _uiState.update { it.copy(isSyncing = false) }
            }
            .onFailure { error ->
                _uiState.update { it.copy(isSyncing = false, errorMessage = error.message ?: "Error syncing properties") }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
