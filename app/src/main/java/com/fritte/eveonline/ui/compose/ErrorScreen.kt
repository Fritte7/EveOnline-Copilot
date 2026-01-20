package com.fritte.eveonline.ui.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorScreen(error: String) {
    Text("An error happens: $error")
}
