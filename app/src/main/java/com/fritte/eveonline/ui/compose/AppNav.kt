package com.fritte.eveonline.ui.compose

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fritte.eveonline.ui.auth.AuthState
import com.fritte.eveonline.ui.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val vm: AuthViewModel = koinViewModel()
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.checkSession() }

    // React to auth state changes
    LaunchedEffect(state) {
        when (state) {
            AuthState.LoggedIn -> navController.navigate("main") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
            AuthState.LoggedOut -> navController.navigate("login") {
                popUpTo(0)
                launchSingleTop = true
            }
            AuthState.Loading -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login" // will be replaced by LaunchedEffect once state resolves
    ) {
        composable("login") { LoginScreen(vm) }
        composable("main") { MainScreen() }
    }
}
