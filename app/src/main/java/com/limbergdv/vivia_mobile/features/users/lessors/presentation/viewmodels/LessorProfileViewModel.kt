package com.limbergdv.vivia_mobile.features.users.lessors.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.LogoutUseCase
import com.limbergdv.vivia_mobile.features.users.lessors.domain.usecases.GetLessorProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessorProfileViewModel @Inject constructor(
    private val getLessorProfileUseCase: GetLessorProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LessorProfileState())
    val state: StateFlow<LessorProfileState> = _state.asStateFlow()

    init {
        onEvent(LessorProfileEvent.LoadProfile)
    }

    fun onEvent(event: LessorProfileEvent) {
        when (event) {
            is LessorProfileEvent.LoadProfile -> loadProfile()
            is LessorProfileEvent.ConsumeError -> _state.update { it.copy(error = null) }
            is LessorProfileEvent.Logout -> logout(event.onLogoutComplete)
        }
    }

    private fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = logoutUseCase()
            if (result.isSuccess) {
                onLogoutComplete()
            } else {
                _state.update { it.copy(isLoading = false, error = result.exceptionOrNull()?.message) }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = getLessorProfileUseCase()
            result.fold(
                onSuccess = { lessor ->
                    _state.update { it.copy(isLoading = false, lessor = lessor) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }
}