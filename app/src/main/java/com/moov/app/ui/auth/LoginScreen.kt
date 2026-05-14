package com.moov.app.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moov.app.R

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit, onLoginSuccess: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_moov),
                contentDescription = "Logo Moov",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Sign in to continue", fontSize = 14.sp, color = Color(0xFFB3B3B3))

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color(0xFFB3B3B3)) },
                leadingIcon = { Icon(Icons.Filled.Email, "Email", tint = Color(0xFFB3B3B3)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFE50914), unfocusedBorderColor = Color(0xFF2E2E2E),
                    focusedContainerColor = Color(0xFF1A1A1A), unfocusedContainerColor = Color(0xFF1A1A1A)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color(0xFFB3B3B3)) },
                leadingIcon = { Icon(Icons.Filled.Lock, "Password", tint = Color(0xFFB3B3B3)) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "Toggle", tint = Color(0xFFB3B3B3)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFE50914), unfocusedBorderColor = Color(0xFF2E2E2E),
                    focusedContainerColor = Color(0xFF1A1A1A), unfocusedContainerColor = Color(0xFF1A1A1A)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Forgot Password?", color = Color(0xFFB3B3B3), fontSize = 14.sp,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Validasi input kosong
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Email dan Password harus diisi!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    authViewModel.login(email, password) { errorMessage ->
                        if (errorMessage != null) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
            ) {
                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text("Don't have an account? ", color = Color(0xFFB3B3B3), fontSize = 14.sp)
                Text(
                    "Sign Up", color = Color(0xFFE50914), fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}