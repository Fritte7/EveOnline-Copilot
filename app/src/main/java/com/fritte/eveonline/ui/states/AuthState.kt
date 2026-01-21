package com.fritte.eveonline.ui.states

sealed interface AuthState {
    data object Loading : AuthState
    data object LoggedOut : AuthState
    data object LoggedIn : AuthState
}