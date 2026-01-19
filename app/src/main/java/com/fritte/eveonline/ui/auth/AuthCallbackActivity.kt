package com.fritte.eveonline.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fritte.eveonline.ui.screens.MainActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AuthCallbackActivity : AppCompatActivity() {

    private val authManager: EveAuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent?.data
        val code = uri?.getQueryParameter("code")
        val state = uri?.getQueryParameter("state")

        if (code.isNullOrBlank() || state.isNullOrBlank()) {
            finish() // cancelled or invalid redirect
            return
        }

        lifecycleScope.launch {
            val ok = authManager.handleAuthCode(code, state)
            val i = Intent(this@AuthCallbackActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(i)
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
