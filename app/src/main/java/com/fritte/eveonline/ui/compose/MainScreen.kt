package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.ui.model.LocationUI
import com.fritte.eveonline.ui.states.UiState
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    locationVm: LocationViewModel = koinViewModel()
) {
    val characterName = locationVm.characterName.collectAsState().value
    val onlineState = locationVm.onlineState.collectAsState().value
    val locationUiState = locationVm.locationUiState.collectAsState().value

    DisposableEffect(Unit) {
        onDispose { locationVm.stopPolling() }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = characterName?.let { "o7 $it" } ?: "o7 Capsuleer") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            StatusCard(
                onlineState = onlineState,
                onRetry = {
                    // simplest: stop/start happens automatically when characterId exists
                    // but you can also expose a refresh() if you prefer
                }
            )

            LocationCard(
                onlineState = onlineState,
                locationUiState = locationUiState
            )
        }
    }
}

@Composable
private fun StatusCard(
    onlineState: UiState<CharacterLocationOnline>,
    onRetry: () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Pilot Status", style = MaterialTheme.typography.titleMedium)

            when (onlineState) {
                UiState.Idle -> {
                    Text("Waiting for authentication / character selection…")
                }

                UiState.Loading -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Checking online status…")
                    }
                }

                is UiState.Success -> {
                    val online = onlineState.data.online
                    val text = if (online) "Online" else "Offline"
                    Text(text = text)

                    // Optional: show extra fields if your model has them (e.g. last_login)
                    // Text("Last login: ${onlineState.data.lastLogin}")
                }

                is UiState.Error -> {
                    Text("Couldn’t fetch online status: ${onlineState.message}")
                    Spacer(Modifier.height(6.dp))
                    OutlinedButton(onClick = onRetry) { Text("Retry") }
                }
            }
        }
    }
}

@Composable
private fun LocationCard(
    onlineState: UiState<CharacterLocationOnline>,
    locationUiState: UiState<LocationUI>
) {
    val isOnline = (onlineState as? UiState.Success)?.data?.online == true

    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Location", style = MaterialTheme.typography.titleMedium)

            // Always show something, even offline
            when (locationUiState) {
                UiState.Idle -> {
                    Text(if (isOnline) "No location yet." else "Offline — no last known location yet.")
                }

                UiState.Loading -> {
                    // Only show loading while online; offline we keep last known
                    if (isOnline) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(10.dp))
                            Text("Updating location…")
                        }
                    } else {
                        Text("Offline — location polling paused.")
                    }
                }

                is UiState.Success -> {
                    val ui = locationUiState.data

                    Text(ui.title)
                    ui.subtitle?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }

                    if (!isOnline) {
                        Text("Offline — location polling paused.")
                    }
                }

                is UiState.Error -> {
                    // Still show offline hint if relevant
                    Text("Couldn’t fetch location: ${locationUiState.message}")
                    if (!isOnline) Text("Offline — location polling paused.")
                }
            }
        }
    }
}

