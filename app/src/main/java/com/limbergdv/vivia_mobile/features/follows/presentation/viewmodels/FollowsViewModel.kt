package com.limbergdv.vivia_mobile.features.follows.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.LogoutUseCase
import com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases.FollowLessorUseCase
import com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases.GetAllLessorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor(
    private val getAllLessorsUseCase: GetAllLessorsUseCase,
    private val followLessorUseCase: FollowLessorUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FollowsState())
    val state: StateFlow<FollowsState> = _state.asStateFlow()

    init {
        // Cargamos la lista en cuanto la pantalla se abre
        onEvent(FollowsEvent.LoadLessors)
    }

    fun onEvent(event: FollowsEvent) {
        when (event) {
            is FollowsEvent.LoadLessors -> loadLessors()
            is FollowsEvent.OnFollowClicked -> followLessor(event.companyName)
            is FollowsEvent.ConsumeError -> _state.update { it.copy(error = null) }
            is FollowsEvent.Logout -> logout(event.onLogoutComplete)
        }
    }

    private fun loadLessors() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // AQUÍ IRÁ LA LLAMADA AL USE CASE:
            val result = getAllLessorsUseCase()
            result.fold(
                onSuccess = { list -> _state.update { it.copy(isLoading = false, lessors = list) } },
                onFailure = { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
            )
        }
    }

    private fun followLessor(companyName: String) {
        viewModelScope.launch {
            // Actualizamos la UI inmediatamente (Optimistic Update) para que el botón reaccione rápido
            _state.update {
                it.copy(followedCompanies = it.followedCompanies + companyName)
            }

            // AQUÍ IRÁ LA LLAMADA AL USE CASE:
            val result = followLessorUseCase(companyName)
            result.onFailure { e ->
            // Si falla, revertimos el cambio visual y mostramos error
                _state.update {
                    it.copy(
                        followedCompanies = it.followedCompanies - companyName,
                        error = "No se pudo seguir a $companyName"
                    )
                }
            }
        }
    }

    private fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            Log.d("FollowsVM", "Cerrando sesión...")
            _state.update { it.copy(isLoading = true) }

            val result = logoutUseCase.invoke()

            if (result.isSuccess) {
                Log.d("FollowsVM", "Sesión cerrada exitosamente")
                onLogoutComplete()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error al cerrar sesión"
                Log.e("FollowsVM", "Error al cerrar sesión: $errorMsg")
                _state.update { it.copy(error = errorMsg, isLoading = false) }
            }
        }
    }
}