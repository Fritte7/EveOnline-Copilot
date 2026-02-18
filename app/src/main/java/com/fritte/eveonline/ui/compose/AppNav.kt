package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.fritte.eveonline.theme.EveColors
import com.fritte.eveonline.ui.nav.Routes
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

    TerminalBackground {
        Scaffold(
            containerColor = EveColors.Transparent,
            contentColor = EveColors.OnBg,
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Screens live here
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.BOOT,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(Routes.BOOT) { BootScreen(Modifier.fillMaxSize(), bootVm) }
                        composable(Routes.LOGIN) { LoginScreen(authVm) }
                        composable(Routes.MAIN) { MainScreen() }
                        composable(Routes.TIM) { TimelineScreen() }
                        composable(
                            Routes.SYSTEM_DETAILS,
                            arguments = listOf(navArgument("systemId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val systemId = backStackEntry.arguments?.getLong("systemId") ?: return@composable
                            // SystemDetailScreen(systemId)
                        }
                    }
                }

                val backStackEntry by navController.currentBackStackEntryAsState()
                val route = backStackEntry?.destination?.route
                val showTerminal = route !in setOf(Routes.BOOT, Routes.LOGIN)
                if (showTerminal) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TerminalNavigation(navController)
                    }
                }
            }
        }
    }

    LaunchedEffect(bootState, authState) {
        val current = navController.currentDestination?.route

        if (!dataReady || !terminalDone) return@LaunchedEffect
        if (bootState is StartupState.Error) return@LaunchedEffect

        when (authState) {
            AuthState.LoggedIn -> if (current != Routes.MAIN) {
                navController.navigate(Routes.MAIN) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
            AuthState.LoggedOut -> if (current != Routes.LOGIN) {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.MAIN) { inclusive = true }
                    launchSingleTop = true
                }
            }
            AuthState.Loading -> Unit
        }
    }
}