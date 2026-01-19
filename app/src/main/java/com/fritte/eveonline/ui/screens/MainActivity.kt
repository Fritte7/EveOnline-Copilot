package com.fritte.eveonline.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.fritte.eveonline.ui.compose.AppNav
import com.fritte.eveonline.theme.EveOnlineCopilotTheme
import org.koin.core.component.getScopeName

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EveOnlineCopilotTheme {
                AppNav()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(MainActivity::class.java.getScopeName().toString(), "onResume")
    }
}