package com.fritte.eveonline.ui.compose

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fritte.eveonline.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel
) {
    val context = LocalContext.current

    LoginScreenContent(
        onLoginClick = {
            val uri = vm.startLogin()
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(context, uri)
        }
    )
}