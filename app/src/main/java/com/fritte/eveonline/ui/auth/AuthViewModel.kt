package com.fritte.eveonline.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.data.repo.TokenStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AuthViewModel(
    private val tokenStore: TokenStore,
    private val authManager: EveAuthManager,
) : ViewModel() {

    val state: StateFlow<AuthState> =
        tokenStore.hasSessionFlow
            .map { has -> if (has) AuthState.LoggedIn else AuthState.LoggedOut }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AuthState.Loading)

    fun startLogin(): android.net.Uri {
        val (uri, _) = authManager.startLogin()
        return uri
    }
}
