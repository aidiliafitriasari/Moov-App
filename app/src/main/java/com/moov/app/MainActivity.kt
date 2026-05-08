package com.moov.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.moov.app.ui.MainScreen
import com.moov.app.ui.auth.LoginScreen
import com.moov.app.ui.auth.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf("login") }

            when (currentScreen) {
                "login" -> LoginScreen(
                    onNavigateToRegister = { currentScreen = "register" },
                    onLoginSuccess = { currentScreen = "main" }
                )
                "register" -> RegisterScreen(
                    onNavigateToLogin = { currentScreen = "login" }
                )
                "main" -> MainScreen()
            }
        }
    }
}