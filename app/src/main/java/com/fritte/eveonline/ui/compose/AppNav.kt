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

    // React to auth state changes
    LaunchedEffect(state) {
        val current = navController.currentDestination?.route
        when (state) {
            AuthState.LoggedIn -> if (current != "main") {
                navController.navigate("main") {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
            AuthState.LoggedOut -> if (current != "login") {
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            }
            AuthState.Loading -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(vm) }
        composable("main") { MainScreen() }
    }
}
