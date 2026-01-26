package com.fritte.eveonline.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.fritte.eveonline.ui.states.UiState
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    locationVm: LocationViewModel = koinViewModel()
) {
    val characterName = locationVm.characterName.collectAsState().value
    val onlineState = locationVm.onlineState.collectAsState().value
    val locationUiState = locationVm.locationUiState.collectAsState().value

    val isOnline = (onlineState as? UiState.Success)?.data?.online == true
    val showRecorded = (locationUiState as? UiState.Success)?.data?.isNewRecorded == true

    var blinking by remember { mutableStateOf(false) }
    val durationBlink = 350
    val alpha by animateFloatAsState(
        targetValue = if (blinking) 0.2f else 1f,
        animationSpec = tween(
            durationMillis = durationBlink,
            easing = LinearEasing
        ),
        label = "blinkAlpha"
    )

    DisposableEffect(Unit) {
        onDispose { locationVm.stopPolling() }
    }

    LaunchedEffect(showRecorded) {
        if (!showRecorded) return@LaunchedEffect
        repeat(3) {
            blinking = true
            delay(durationBlink.toLong())
            blinking = false
            delay(durationBlink.toLong())
        }
    }

    TerminalScaffold {
        TerminalPanel {
            TerminalRow("Captain", characterName ?: "Capsuleer", statusColor("OK"))
            when (onlineState) {
                UiState.Idle -> {}
                UiState.Loading ->  TerminalRow("Status", "CHECKING", valueColor = statusColor("WARN"))
                is UiState.Error -> TerminalRow("Status", "ERROR: ${onlineState.message}", valueColor = statusColor("ERROR"))
                is UiState.Success -> {
                    TerminalRow("Status", if (isOnline) "ONLINE" else "OFFLINE", valueColor = statusColor(if (isOnline) "ONLINE" else "OFFLINE"))
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
                    Spacer(Modifier.height(12.dp))

                    TerminalLine("Location:")
                    TerminalRow("System", ui.systemName ?: "unknown", valueColor = statusColor("OK"))
                    TerminalRow("Class", ui.systemClass ?: "", valueColor = statusColor("OK"))
                    TerminalRow("Effect", ui.systemEffect ?: "", valueColor = statusColor("OK"))
                }

                is UiState.Error -> {
                    TerminalLine("Location error: ${locationUiState.message}", color = statusColor("ERROR"))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        AnimatedVisibility(
            visible = showRecorded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TerminalPanel(modifier = Modifier.alpha(alpha)) {
                TerminalRow(
                    label = "New system discovered",
                    value = "RECORDED",
                    valueColor = statusColor("WARN")
                )
            }
        }
    }
}
