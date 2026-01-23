package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.ui.states.UiState
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    locationVm: LocationViewModel = koinViewModel()
) {
    val characterName = locationVm.characterName.collectAsState().value
    val onlineState = locationVm.onlineState.collectAsState().value
    val locationUiState = locationVm.locationUiState.collectAsState().value

    val isOnline = (onlineState as? UiState.Success)?.data?.online == true

    DisposableEffect(Unit) {
        onDispose { locationVm.stopPolling() }
    }

    TerminalScaffold(
        title = characterName?.let { "Captain $it" } ?: "Captain Capsuleer"
    ) {
        TerminalPanel {
            TerminalLine("Handshake: CONCORD relay ...... ${onlineValue(onlineState)}",
                color = statusColor(onlineValue(onlineState))
            )

            when (onlineState) {
                UiState.Idle -> TerminalLine("Awaiting authentication / character selection …", color = statusColor("PAUSED"))
                UiState.Loading -> TerminalLine("Checking online status …", color = statusColor("WARN"))
                is UiState.Error -> TerminalLine("Online status error: ${onlineState.message}", color = statusColor("ERROR"))
                is UiState.Success -> {
                    TerminalRow("Pilot status", if (isOnline) "ONLINE" else "OFFLINE", valueColor = statusColor(if (isOnline) "ONLINE" else "OFFLINE"))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        TerminalPanel {
            TerminalRow(
                label = "Location polling",
                value = if (isOnline) "ACTIVE" else "PAUSED",
                valueColor = statusColor(if (isOnline) "OK" else "PAUSED")
            )

            when (locationUiState) {
                UiState.Idle -> {
                    //TerminalLine(if (isOnline) "No location yet." else "Offline — no last known location yet.", color = statusColor("PAUSED"))
                }

                UiState.Loading -> {
                    TerminalLine(
                        if (isOnline) "Updating location …" else "Offline — location polling paused.",
                        color = statusColor(if (isOnline) "WARN" else "PAUSED")
                    )
                }

                is UiState.Success -> {
                    val ui = locationUiState.data
                    TerminalRow("Space", ui.title, valueColor = statusColor("OK"))

                    TerminalRow("Detail", "", valueColor = statusColor("OK"))
                    Spacer(Modifier.height(12.dp))
                    TerminalRow("System", ui.systemName ?: "unknown", valueColor = statusColor("OK"))
                    TerminalRow("Class", ui.systemClass ?: "", valueColor = statusColor("OK"))
                    TerminalRow("Effect", ui.systemEffect ?: "", valueColor = statusColor("OK"))
                }

                is UiState.Error -> {
                    TerminalLine("Location error: ${locationUiState.message}", color = statusColor("ERROR"))
                }
            }
        }
    }
}

private fun onlineValue(state: UiState<CharacterLocationOnline>): String = when (state) {
    UiState.Idle -> "PAUSED"
    UiState.Loading -> "…"
    is UiState.Success -> if (state.data.online) "OK" else "OFFLINE"
    is UiState.Error -> "FAIL"
}
