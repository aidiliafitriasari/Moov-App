package com.moov.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.moov.app.ui.auth.LoginScreen
import com.moov.app.ui.auth.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showRegister by remember { mutableStateOf(false) }

            if (showRegister) {
                RegisterScreen(onNavigateToLogin = { showRegister = false })
            } else {
                LoginScreen(onNavigateToRegister = { showRegister = true })
            }
        }
    }
}