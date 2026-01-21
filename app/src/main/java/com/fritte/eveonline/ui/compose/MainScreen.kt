package com.fritte.eveonline.ui.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    locationVm: LocationViewModel = koinViewModel()
) {

    val characterName = locationVm.characterName.collectAsState().value

    DisposableEffect(Unit) {
        onDispose { locationVm.stopPolling() }
    }

    Text(
        text = characterName?.let { "Welcome $it" } ?: "Welcome Capsuleer"
    )
}