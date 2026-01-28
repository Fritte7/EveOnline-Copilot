package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fritte.eveonline.ui.nav.Cmd
import com.fritte.eveonline.ui.nav.NavEvent
import com.fritte.eveonline.ui.nav.Routes
import com.fritte.eveonline.ui.viewmodel.NavigationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TerminalNavigation(
    navController: NavHostController,
    navBus: NavigationViewModel = koinViewModel()
) {
    var line by remember { mutableStateOf("") }

    // 1) Navigation execution lives here (single place)
    LaunchedEffect(Unit) {
        navBus.nav.collect { ev ->
            when (ev) {
                NavEvent.ToMain -> navController.navigate(Routes.MAIN) { launchSingleTop = true }
                NavEvent.ToTim -> navController.navigate(Routes.TIM) { launchSingleTop = true }
                NavEvent.ToDb -> navController.navigate(Routes.DB) { launchSingleTop = true }
                NavEvent.ToCfg -> navController.navigate(Routes.CFG) { launchSingleTop = true }
                NavEvent.ToSta -> navController.navigate(Routes.STA) { launchSingleTop = true }
                is NavEvent.ToSystemDetails -> navController.navigate(Routes.systemDetails(ev.systemId))
                NavEvent.Back -> navController.popBackStack()
            }
        }
    }

    TerminalKeypadBackground {
        Column {
            TerminalCommandLine(
                value = line,
                onValueChange = { line = it },
                onSubmit = {
                    navBus.submitLine(line)
                    line = ""
                }
            )

            Spacer(Modifier.height(8.dp))

            // Buttons
            TerminalKeypad(
                onNav = { navBus.submitLine(Cmd.CD +" "+ Cmd.NAV) },
                onDb = { navBus.submitLine(Cmd.CD +" "+ Cmd.DB) },
                onSta = { navBus.submitLine(Cmd.CD +" "+ Cmd.STA) },
                onCfg = { navBus.submitLine(Cmd.CD +" "+ Cmd.CFG) },
                onTim = { navBus.submitLine(Cmd.CD +" "+ Cmd.TIM) },
                onTab = { /* autocomplete later */ },
                onEnter = {
                    navBus.submitLine(line)
                    line = ""
                }
            )
        }
    }
}
