package com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.LogoutUseCase
import com.limbergdv.vivia_mobile.features.properties.remote.domain.entities.Property
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.ObserveLesseePropertiesUseCase
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.SyncLesseePropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LesseePropertiesViewModel @Inject constructor(
    private val observeUseCase: ObserveLesseePropertiesUseCase,
    private val syncUseCase: SyncLesseePropertiesUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    data class UiState(
        val properties: List<Property> = emptyList(),
        val isSyncing: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeProperties()
    }

    private fun observeProperties() {
        viewModelScope.launch {
            observeUseCase().collect { list ->
                _uiState.update { it.copy(properties = list) }
            }
        }
    }

    fun sync() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            syncUseCase().onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
            _uiState.update { it.copy(isSyncing = false) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onComplete()
        }
    }
}
