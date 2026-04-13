package com.limbergdv.vivia_mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limbergdv.vivia_mobile.core.session.TokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthState {
    object Loading : AuthState
    data class Authenticated(val userType: TokenDataStore.UserType) : AuthState
    object Unauthenticated : AuthState
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            tokenDataStore.accessTokenFlow
                .collect { token ->
                    if (token != null) {
                        val userType = tokenDataStore.getUserType() ?: TokenDataStore.UserType.LESSEE
                        _authState.value = AuthState.Authenticated(userType)
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }
                }
        }
    }
}
