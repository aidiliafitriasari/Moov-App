package com.moov.app.ui.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun FavoriteScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0D0D0D)),
        contentAlignment = Alignment.Center
    ) {
        Text("Favorite", color = Color.White, fontSize = 24.sp)
    }
}