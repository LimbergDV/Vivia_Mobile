package com.limbergdv.vivia_mobile.features.follows.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.features.follows.domain.usecases.GetFollowersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val getFollowersUseCase: GetFollowersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FollowersState())
    val state: StateFlow<FollowersState> = _state.asStateFlow()

    init {
        onEvent(FollowersEvent.LoadFollowers)
    }

    fun onEvent(event: FollowersEvent) {
        when (event) {
            is FollowersEvent.LoadFollowers -> loadFollowers()
            is FollowersEvent.ConsumeError -> _state.update { it.copy(error = null) }
        }
    }

    private fun loadFollowers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = getFollowersUseCase()
            result.fold(
                onSuccess = { list ->
                    _state.update { it.copy(isLoading = false, followers = list) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }
}