package com.fritte.eveonline.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.fritte.eveonline.ui.states.StartupState
import com.fritte.eveonline.ui.viewmodel.StartupViewModel

@Composable
fun BootScreen(
    vm: StartupViewModel,
    onReady: () -> Unit,
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.boot()
    }

    when (state) {
        StartupState.Booting -> TerminalBoot()
        StartupState.ImportingStaticData -> {  } // Update TerminalBoot?
        StartupState.CheckingAuth -> {  }        // Update TerminalBoot?
        StartupState.Ready -> onReady()
        is StartupState.Error -> ErrorScreen((state as StartupState.Error).message)
    }
}