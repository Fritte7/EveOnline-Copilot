package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import com.fritte.eveonline.theme.EveColors
import com.fritte.eveonline.ui.states.AuthState
import com.fritte.eveonline.ui.viewmodel.AuthViewModel
import com.fritte.eveonline.ui.states.StartupState
import com.fritte.eveonline.ui.viewmodel.StartupViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val bootVm: StartupViewModel = koinViewModel()
    val authVm: AuthViewModel = koinViewModel()
    val bootState by bootVm.state.collectAsStateWithLifecycle()
    val authState by authVm.state.collectAsStateWithLifecycle()
    val dataReady by bootVm.dataReady.collectAsStateWithLifecycle()
    val terminalDone by bootVm.terminalDone.collectAsStateWithLifecycle()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route

    TerminalBackground {
        Scaffold(
            containerColor = EveColors.Transparent,
            contentColor = EveColors.OnBg,
            topBar = {

                when (route) {
                    "main" -> {
                        TerminalTopBar(
                            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                            title = "EVE Online Copilot",
                            subtitle = "Stay Neutral."
                        )
                    }
                    else -> {
                        // nothing yet
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "boot",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("boot") {
                    BootScreen(
                        modifier = Modifier.fillMaxSize(),
                        bootVm,
                    )
                }
                composable("login") {
                    LoginScreen(authVm)
                }
                composable("main") {
                    MainScreen()
                }
            }
        }
    }

    LaunchedEffect(bootState, authState) {
        val current = navController.currentDestination?.route

        if (!dataReady || !terminalDone) return@LaunchedEffect
        if (bootState is StartupState.Error) return@LaunchedEffect

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