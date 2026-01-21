package com.fritte.eveonline.ui.compose

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fritte.eveonline.ui.auth.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel
) {
    val context = LocalContext.current

    Button(
        onClick = {
            val uri = vm.startLogin()

            val intent = CustomTabsIntent.Builder().build()
            intent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.launchUrl(context, uri)
        }
    ) {
        Text("Log in with EVE Online")
    }
}