package com.fritte.eveonline.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.data.network.TokenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val tokenStore: TokenStore,
    private val authManager: EveAuthManager,
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)
    val state: StateFlow<AuthState> = _state

    fun checkSession() {
        viewModelScope.launch {
            _state.value = if (tokenStore.hasRefreshToken()) AuthState.LoggedIn else AuthState.LoggedOut
        }
    }

    fun startLogin(): android.net.Uri {
        val (uri, _) = authManager.startLogin()
        return uri
    }

    // Called after AuthCallbackActivity finishes and app resumes
    fun onAuthResult() {
        checkSession()
    }
}
