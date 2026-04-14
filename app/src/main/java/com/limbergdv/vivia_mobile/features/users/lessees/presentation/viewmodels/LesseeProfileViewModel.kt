package com.limbergdv.vivia_mobile.features.users.lessees.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.auth.domain.usecases.LogoutUseCase
import com.limbergdv.vivia_mobile.features.users.lessees.domain.usecases.GetLesseeProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LesseeProfileViewModel @Inject constructor(
    private val getLesseeProfileUseCase: GetLesseeProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LesseeProfileState())
    val state: StateFlow<LesseeProfileState> = _state.asStateFlow()

    init {
        onEvent(LesseeProfileEvent.LoadProfile)
    }

    fun onEvent(event: LesseeProfileEvent) {
        when (event) {
            is LesseeProfileEvent.LoadProfile -> loadProfile()
            is LesseeProfileEvent.ConsumeError -> _state.update { it.copy(error = null) }
            is LesseeProfileEvent.Logout -> logout(event.onLogoutComplete)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getLesseeProfileUseCase().fold(
                onSuccess = { lessee ->
                    _state.update { it.copy(isLoading = false, lessee = lessee) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
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
}
