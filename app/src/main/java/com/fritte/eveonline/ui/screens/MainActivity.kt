package com.fritte.eveonline.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fritte.eveonline.ui.compose.AppNav
import com.fritte.eveonline.theme.EveOnlineCopilotTheme

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EveOnlineCopilotTheme {
                AppNav()
            }
        }
    }
}