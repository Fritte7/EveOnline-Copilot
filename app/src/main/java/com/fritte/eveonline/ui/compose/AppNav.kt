package com.fritte.eveonline.ui.compose

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fritte.eveonline.ui.auth.AuthState
import com.fritte.eveonline.ui.auth.AuthViewModel
import com.fritte.eveonline.ui.states.StartupState
import com.fritte.eveonline.ui.viewmodel.StartupViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val bootVm: StartupViewModel = koinViewModel()
    val authVm: AuthViewModel = koinViewModel()
    val bootState by bootVm.state.collectAsStateWithLifecycle()
    val authState by authVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "boot"
    ) {
        composable("boot") {
            BootScreen(
                bootVm,
                onReady = {
                    navController.navigate("gate") {
                        popUpTo("boot") { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable("gate") { GateScreen() }
        composable("login") { LoginScreen(authVm) }
        composable("main") { MainScreen() }
    }

    LaunchedEffect(bootState, authState) {
        val current = navController.currentDestination?.route

        if (bootState != StartupState.Ready) return@LaunchedEffect

        when (authState) {
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
}
