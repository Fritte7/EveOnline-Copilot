package com.fritte.eveonline.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fritte.eveonline.domain.repository.ForegroundTracker
import com.fritte.eveonline.ui.compose.AppNav
import com.fritte.eveonline.theme.EveOnlineCopilotTheme
import org.koin.android.ext.android.inject

class MainActivity: ComponentActivity() {

    private val foregroundTracker: ForegroundTracker by inject()

    override fun onStart() {
        super.onStart()
        foregroundTracker.setForeground(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EveOnlineCopilotTheme {
                AppNav()
            }
        }
    }

    override fun onLocalVoiceInteractionStopped() {
        foregroundTracker.setForeground(false)
        super.onLocalVoiceInteractionStopped()
    }
}