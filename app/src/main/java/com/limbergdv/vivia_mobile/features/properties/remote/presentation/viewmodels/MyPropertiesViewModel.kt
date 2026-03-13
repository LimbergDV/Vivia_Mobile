package com.limbergdv.vivia_mobile.features.properties.remote.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.ObserveMyPropertiesUseCase
import com.limbergdv.vivia_mobile.features.properties.remote.domain.usecases.SyncMyPropertiesUseCase
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
        Log.d("MyPropertiesVM", "ViewModel inicializado. Ejecutando init...")
        observeProperties()
        syncProperties() // Llamada directa apenas se abre la pantalla
    }

    private fun observeProperties() {
        observeMyPropertiesUseCase()
            .onEach { properties ->
                Log.d("MyPropertiesVM", "Room detectó cambios. Enviando ${properties.size} propiedades a la UI.")
                _uiState.update { it.copy(properties = properties) }
            }
            .launchIn(viewModelScope)
    }

    fun syncProperties() {
        viewModelScope.launch {
            Log.d("MyPropertiesVM", "Iniciando UI Sync (isSyncing = true)")
            _uiState.update { it.copy(isSyncing = true, errorMessage = null) }

            val result = syncMyPropertiesUseCase.invoke("")

            if (result.isSuccess) {
                Log.d("MyPropertiesVM", "Sincronización finalizada con éxito desde el UseCase.")
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                Log.e("MyPropertiesVM", "Sincronización falló: $errorMsg")
                _uiState.update { it.copy(errorMessage = errorMsg) }
            }

            Log.d("MyPropertiesVM", "Finalizando UI Sync (isSyncing = false)")
            _uiState.update { it.copy(isSyncing = false) }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
